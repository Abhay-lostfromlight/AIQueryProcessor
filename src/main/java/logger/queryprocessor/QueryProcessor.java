package logger.queryprocessor;

public interface QueryProcessor {
    /**
     * Processes a given query on the specified source.
     * @param query query to be procesed. it could be SELECT or FROM etc
     * @param source the source on which we are querying, be database or file.
     */
    void processor(String query, Object source) throws Exception;
}
