package logger.io;

import logger.entity.Log;

import java.util.Collection;
import java.util.List;

public class FileReader implements Reader {
    /**
     * @param source
     * @return
     */
    @Override
    public Collection<Log> read(Object source) {
        return List.of();
    }
}
