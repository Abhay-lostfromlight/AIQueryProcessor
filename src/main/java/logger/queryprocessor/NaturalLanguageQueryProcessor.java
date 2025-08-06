package logger.queryprocessor;

import com.google.api.client.http.HttpResponseException;

import java.io.IOException;

public class NaturalLanguageQueryProcessor implements QueryProcessor {
    private final NaturalLanguageToSQLTranslator translator;

    public NaturalLanguageQueryProcessor(String geminiApiKey) {
        this.translator = new NaturalLanguageToSQLTranslator(geminiApiKey);
    }

    @Override
    public void process(String query, Object source) throws Exception {
        try {
            System.out.println("=== Natural Language Query Processing ===");
            System.out.println("Natural Language Query: " + query);

            String sql = translator.translate(query);
            System.out.println("AI-generated SQL: " + sql);

            // Display the generated SQL for the user
            System.out.println("\u001B[32m✓ Query successfully translated to SQL\u001B[0m");

            // Execute the generated SQL automatically
            System.out.println("\u001B[36m📋 Executing SQL query on log file...\u001B[0m");
            SelectQueryProcessor selectProcessor = new SelectQueryProcessor();
            selectProcessor.process(sql, source);
            System.out.println("\u001B[32m✓ Query execution completed\u001B[0m");

        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 429) {
                System.err.println("Rate limit exceeded. Please wait before making more requests.");
                System.err.println("Consider upgrading your API plan for higher quotas.");
            } else {
                System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getStatusMessage());
            }
            throw e;
        } catch (IOException e) {
            System.err.println("Network error occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Failed to process natural language query: " + e.getMessage());
            throw e;
        }
    }

}

