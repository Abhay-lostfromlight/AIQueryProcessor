package logger.io;

import logger.pojo.Log;

import java.util.Collection;

public interface Reader {
    Collection<Log> read(Object source);
}
