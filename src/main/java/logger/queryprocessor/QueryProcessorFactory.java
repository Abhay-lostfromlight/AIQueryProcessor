package logger.queryprocessor;

import logger.config.ApiConfig;

import java.util.HashMap;
import java.util.Map;

public class QueryProcessorFactory {

    public static Map<String, QueryProcessor>
    queryProcessorMap =  new HashMap<>();

    static {
        queryProcessorMap.put("select", new SelectQueryProcessor());
        queryProcessorMap.put("find", new FindQueryProcessor());
    }

    //get query, split it
    //if  queryprocessormap doesn't contain the key; command, throw exception
    //else return the queryprocessor for that command
    public QueryProcessor getQueryProcessor(String query) throws Exception{
        String[] queryElements = query.split(" ");
        String command = queryElements[0].toLowerCase();
        if(!queryProcessorMap.containsKey(command)){
            throw new RuntimeException("No queryProcessor found for command: " + command);
        }
        return queryProcessorMap.get(command);
    }

    // get corresponding query processor based on type
    //if nlp, get api key, return NaturalLanguageQueryProcessor, default throw exception
    public static QueryProcessor getProcessor(String type) {
        switch (type.toLowerCase()) {
            case "select":
                return new SelectQueryProcessor();
            case "find":
                return new FindQueryProcessor();
            case "nlp":
                // Get API key securely from configuration
                String apiKey = ApiConfig.getInstance().getGeminiApiKey();
                return new NaturalLanguageQueryProcessor(apiKey);
            default:
                throw new IllegalArgumentException("Unknown processor type: " + type);
        }
    }

    // Backward compatibility method
    @Deprecated
    public static QueryProcessor getProcessor(String type, String geminiApiKey) {
        if ("nlp".equalsIgnoreCase(type)) {
            return new NaturalLanguageQueryProcessor(geminiApiKey);
        }
        return getProcessor(type);
    }
}


