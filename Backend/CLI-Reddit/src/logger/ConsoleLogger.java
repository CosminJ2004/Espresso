package logger;

public class ConsoleLogger implements ILogger {
    private LogLevel minLevel;

    public ConsoleLogger(LogLevel minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public void log(LogLevel level, String message) {
        if (level.ordinal() >= minLevel.ordinal()) {
            System.out.println("[" + level + "] " + message);
        }
    }
}