package com.example.demo.util.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements ILogger {
    private final LogLevel minLevel;
    private final String filePath;
    private final DateTimeFormatter formatter;

    public FileLogger(LogLevel minLevel, String filePath) {
        this.minLevel = minLevel;
        this.filePath = filePath;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = timestamp + " - " + "[" + level + "] " + message + "\n";

        if (level.ordinal() >= minLevel.ordinal()) {
            try (FileWriter writer = new FileWriter(filePath, true)){
                writer.write(logEntry);
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
    }
}

