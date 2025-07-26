package logger.io;

import logger.pojo.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileReader implements Reader {

//    Take a file path as input.
//    Open the file for reading as a stream of Java objects.
//    While you can read more logs from the stream:
//    Add each Log object to a list.
//    If you reach the end of the file, stop.
//    Always close the file after reading, even if there were errors.
//    Return the list of log objects you got.

    @Override
    public Collection<Log> read(Object source) {

        List<Log> logs = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            String filePath = (String) source;
            File file = new File(filePath);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            while (true) {
                try {
                    Log log = (Log) ois.readObject();
                    logs.add(log);
                } catch (EOFException e) {
                    System.out.println("EOFException");
                    break;
                }
            }
        } catch (Exception ex) {
        } finally {
            try {
                fis.close();
                ois.close();
            } catch (Exception e) {
                System.out.println("not able to close the streams");
            }
        }
        return logs;
    }

}
