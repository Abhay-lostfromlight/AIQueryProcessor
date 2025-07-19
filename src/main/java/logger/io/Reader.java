package logger.io;

import logger.entity.Log;

import java.util.Collection;

public interface Reader {
    Collection<Log> read(Object source);
}
