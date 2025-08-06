package logger.queryprocessor;

public interface QueryProcessor {
    /**
     * Processes a given query on the specified source.
     * @param query query to be processed. it could be SELECT, FROM etc
     * @param source the source on which we are querying, Be it database or a file.
     */
    void process(String query, Object source) throws Exception;

    // Default implementation for processors that don't need a source
    default void process(String query) {
        try {
            process(query, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
