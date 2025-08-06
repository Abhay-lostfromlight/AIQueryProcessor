package logger.queryprocessor;

import logger.io.FileReader;
import logger.io.Reader;
import logger.pojo.Log;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FindQueryProcessor implements QueryProcessor {

    private Reader reader = new FileReader();

    //extract search term from the query, then read logs from the source,
    //filter logs that contain the search term, and print the matching logs.
    @Override
    public void process(String query, Object source) throws Exception {
        System.out.println("=== Find Query Processing ===");
        System.out.println("Query: " + query);
        try {

            String term = extractSearchTerm(query);

            Collection<Log> logs = reader.read(source);
            List<Log> matchingLogs = logs.stream()
                    .filter(log -> log.getData().contains(term))
                    .collect(Collectors.toList());

            System.out.println("Found " + matchingLogs.size() + " log entries.");
            for (Log log : matchingLogs) {
                System.out.println(log.getThreadName() + ": " + log.getData());
            }
        } catch (Exception e) {
            System.err.println("Failed to process find query: " + e.getMessage());
            throw e;
        }
    }

    //split the string into parts and extract the search term
    private String extractSearchTerm(String query) {

        String[] parts = query.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid query format. Expected 'FIND term'.");
        }
        return parts[1];
    }
}
