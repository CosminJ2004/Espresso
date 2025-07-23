package com.example.demo.util.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements ILogger {
    private final LogLevel minLevel;
    private final DateTimeFormatter formatter;

    public ConsoleLogger(LogLevel minLevel) {
        this.minLevel = minLevel;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = timestamp + " - " + "[" + level + "] " + message + "\n";

        if (level.ordinal() >= minLevel.ordinal()) {
            System.out.println(logEntry);
        }
    }
}