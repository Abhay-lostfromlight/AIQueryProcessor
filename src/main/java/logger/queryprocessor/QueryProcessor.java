package logger.queryprocessor;

public interface QueryProcessor {
    /**
     * Processes a given query on the specified source.
     * @param query query to be processed. it could be SELECT, FROM etc
     * @param source the source on which we are querying, Be it database or a file.
     */
    void processor(String query, Object source) throws Exception;
}
