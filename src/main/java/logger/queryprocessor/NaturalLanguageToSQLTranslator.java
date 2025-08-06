package logger.queryprocessor;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalLanguageToSQLTranslator {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY_MS = 1000; // 1 second base delay
    private final String apiKey;
    private final HttpTransport httpTransport = new NetHttpTransport(); //NetHttpTransport is  default HTTP transport for Java from Google libraries
    private final JsonFactory jsonFactory = new GsonFactory();
    private long lastRequestTime = 0;
    private static final long MIN_REQUEST_INTERVAL_MS = 2000;

    public NaturalLanguageToSQLTranslator(String apiKey)
    {
        this.apiKey = apiKey;
    }

    // This method takes a nl query as input and returns the corresponding SQL query as output.
    // If any network or API error occurs, it will throw an IOException back to the caller.
    public String translate(String naturalLanguageQuery) throws IOException {

        //1)try sending the request upto max tries then get current time and calculate how much more we need to wait
        // before making another request for rate limiting.
        //2)if we've made request too recently, sleep thread until waitTime to avoidd rate limiting
        //3)update lastRequestTime to current time
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            long currentTime = System.currentTimeMillis();
            long waitTime = MIN_REQUEST_INTERVAL_MS - (currentTime - lastRequestTime);
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            lastRequestTime = System.currentTimeMillis();

            // prepare the HTTP request to send to the gemini api :
            //   creating the requeest factory, build the prompt with natural language query,
            //   formatting this prompt as a JSON body, build the api urr = endpoints + apikey
            //  wrap the json string as http content, build the post request, then send the request wait
            try {
                HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
                String prompt = "Convert the following natural language request to an SQL query: " + naturalLanguageQuery;

                String requestBody = "{" + "\"contents\": [{\"parts\": [{\"text\": \"" + prompt.replace("\"", "\\\"") + "\"}]}]" + "}";

                GenericUrl url = new GenericUrl(GEMINI_API_URL + "?key=" + apiKey);
                HttpContent content = new ByteArrayContent("application/json", requestBody.getBytes());
                HttpRequest request = requestFactory.buildPostRequest(url, content);
                HttpResponse response = request.execute();

                //check if api says we're sending too many requests, if yes then increment retry count,
                // calculate exponential backoff delay (base delay * retry count> 1, sleep for that delay,
                // then restore interrupted state if interrupted
                // continue the loop and try again
                if (response.getStatusCode() == 429) { // Too Many Requests
                    retryCount++;
                    long retryDelay = BASE_DELAY_MS * (1 << retryCount);
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", ie);
                    }
                    continue;
                }

                //parse the response body as a string, then extract the SQL query from the response
                String responseString = response.parseAsString();
                return extractSQLFromResponse(responseString);

                //handle any io and network error that can occur during request creation
                //if retry limit is reached, throw the exception, else increase delay time and sleep
                //catch restore interrupted state if interrupted
                // if all fails the throw exception
            } catch (IOException e) {
                if (++retryCount >= MAX_RETRIES) {
                    throw e;
                }
                try {
                    Thread.sleep(BASE_DELAY_MS * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Retry interrupted", ie);
                }
            }
        }
        throw new IOException("Failed to process after maximum retries");
    }

    // This method extracts the SQL query from the response string.
    // It uses regex patterns to find the SQL query in various formats, including: unescaped strings, commoon Unicode escapes etc
    private String extractSQLFromResponse(String responseString) {
        String unescaped = responseString.replace("\\n", " ")
                .replace("\\u003e", ">")
                .replace("\\u0026", "&")
                .replace("\\u003c", "<");

        Pattern[] patterns = {

                Pattern.compile("SELECT.*?(?=\\s*[;`\"\\n]|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
                Pattern.compile("```sql\\s*(SELECT.*?)\\s*```", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
                Pattern.compile("(SELECT\\s+.*?)(?=\\s*[;`\"\\n]|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(unescaped);
            if (matcher.find()) {
                String sql = matcher.group(1 <= matcher.groupCount() ? 1 : 0).trim();

                sql = sql.replaceAll("[;`\"]*\\s*$", "").trim();
                if (sql.length() > 0 && sql.toUpperCase().startsWith("SELECT")) {
                    return sql;
                }
            }
        }


        int start = unescaped.toUpperCase().indexOf("SELECT");
        if (start != -1) {

            String[] terminators = {";", "`", "\n", "```"};
            int end = unescaped.length();

            for (String terminator : terminators) {
                int termPos = unescaped.indexOf(terminator, start);
                if (termPos != -1 && termPos < end) {
                    end = termPos;
                }
            }

            return unescaped.substring(start, end).trim();
        }

        return unescaped;
    }

}
