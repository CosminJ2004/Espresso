package logger;

import java.util.ArrayList;
import java.util.List;

public class LoggerManager {
    private List<ILogger> loggers = new ArrayList<>();

    public void addLogger(ILogger logger) {
        loggers.add(logger);
    }

    public void log(LogLevel level, String message) {
        for (ILogger logger : loggers) {
            logger.log(level, message);
        }
    }
}
