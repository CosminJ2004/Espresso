package com.example.backend.util.logger;


public class LoggerFactory {
    public static ILogger create(LoggerType type, String... args) {
        return switch (type) {
            case CONSOLE -> new ConsoleLogger(LogLevel.DEBUG);
            case FILE -> {
                if (args.length == 0) throw new IllegalArgumentException("File path required for FILE logger.");
                yield new FileLogger(LogLevel.DEBUG,args[0]);
            }
        };
    }
}
