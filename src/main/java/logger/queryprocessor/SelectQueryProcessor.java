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
    public void processor(String query, Object source) throws Exception {

        List<String> queryElements = Arrays.asList(query.split(" "));

        Timestamp startTime = null;
        Timestamp endTime = null;
        String data = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(queryElements.contains("timestamp")){
            Date startDate = dateFormat.parse(queryElements.get(queryElements.indexOf("between") +1));
            startTime = new Timestamp(startDate.getTime());

            Date endDate = dateFormat.parse(queryElements.get(queryElements.indexOf("between") +3));
            endTime = new Timestamp(endDate.getTime());

        }

        if(queryElements.contains("like") && queryElements.contains("data")){
            data = queryElements.get(queryElements.indexOf("like") +1);
        }

        Collection<Log> logs = reader.read(source);
        filter(logs, data, startTime, endTime);

    }

    private void filter(Collection<Log> logs, String data, Timestamp startTime, Timestamp endTime) {
        for (Log log : logs) {
            System.out.println(log.getThreadName()+ ": " + log.getData());
        }
    }
}
