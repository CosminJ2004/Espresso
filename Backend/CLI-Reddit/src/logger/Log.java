package logger;

public class Log {
    public static void RegisterLogger(ILogger logger) {
        LoggerManager.getInstance().addLogger(logger);
    }

    public static void log(LogLevel level, String message) {
        LoggerManager.getInstance().log(level, message);
    }
}
