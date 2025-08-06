package logger.queryprocessor;

import logger.pojo.Log;
import logger.io.FileReader;
import logger.io.Reader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SelectQueryProcessor implements QueryProcessor {

    private Reader reader = new FileReader();

    @Override
    public void process(String query, Object source) throws Exception{
        // EXTENSION
        List<String> queryElements = Arrays.asList(query.split(" "));

        // data like "", timestamp between, order by
        Timestamp startTime = null;
        Timestamp endTime = null;
        String data = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(queryElements.contains("timestamp")){
            Date startDate = dateFormat.parse(queryElements.get(queryElements.indexOf("between")+1));
            startTime = new Timestamp(startDate.getTime());

            Date endDate = dateFormat.parse(queryElements.get(queryElements.indexOf("between")+3));
            endTime = new Timestamp(endDate.getTime());
        }
        if(queryElements.contains("like") && queryElements.contains("data")){
            data = queryElements.get(queryElements.indexOf("like")+1);
        }
        System.out.println("\u001B[90mDebug: Reading from file: " + source + "\u001B[0m");
        Collection<Log> logs = reader.read(source);
        System.out.println("\u001B[90mDebug: Found " + logs.size() + " log entries in file\u001B[0m");
        System.out.println("\u001B[90mDebug: Query elements: " + queryElements + "\u001B[0m");
        System.out.println("\u001B[90mDebug: Data filter: '" + data + "', Start time: " + startTime + "\u001B[0m");
        filter(logs, data, startTime, endTime);
    }

    // Filter logs based on data and timestamp criteria
    // If startTime is null, it will not filter by date.
    // If data is empty, it will not filter by data.
    // compare the dates and data in the logs with the query parameters.
    // Print the matching logs in the format: timestamp [severity] data
    private void filter(Collection<Log> logs, String data, Timestamp startTime, Timestamp endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int matchCount = 0;
        for (Log log : logs) {
            boolean matchesDate = true;
            boolean matchesData = true;

            if (startTime != null) {
                String logDate = dateFormat.format(log.getTimestamp());
                String queryDate = dateFormat.format(new Date(startTime.getTime()));
                matchesDate = logDate.equals(queryDate);
            }

            if (!data.isEmpty()) {
                matchesData = log.getData().contains(data);
            }

            if (matchesDate && matchesData) {
                System.out.println(log.getTimestamp() + " [" + log.getSeverity() + "] " + log.getData());
            }
        }
    }
}
