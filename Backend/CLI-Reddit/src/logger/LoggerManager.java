package logger;

import java.util.ArrayList;
import java.util.List;

public class LoggerManager {
    private static LoggerManager instance;
    private final List<ILogger> loggers = new ArrayList<>();

    private LoggerManager() {}
    public void addLogger(ILogger logger) {
        loggers.add(logger);
    }

    public static LoggerManager getInstance() {
        if (instance == null) {
            instance = new LoggerManager();
        }
        return instance;
    }

    public void log(LogLevel level, String message) {
        for (ILogger logger : loggers) {
            logger.log(level, message);
        }
    }
}
