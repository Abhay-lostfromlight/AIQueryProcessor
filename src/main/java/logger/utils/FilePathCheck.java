package logger.utils;

import java.io.File;

public class FilePathCheck {

    public static boolean validFilePath(String filePath) {
        File file = new File(filePath);
        if(!file.exists()){
            return false;
        }
        return true;
    }
}
