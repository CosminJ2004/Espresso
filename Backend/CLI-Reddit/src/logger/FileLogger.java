package logger;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ILogger {
    private LogLevel minLevel;
    private String filePath;

    public FileLogger(LogLevel minLevel, String filePath) {
        this.minLevel = minLevel;
        this.filePath = filePath;
    }

    @Override
    public void log(LogLevel level, String message) {
        if (level.ordinal() >= minLevel.ordinal()) {
            try (FileWriter writer = new FileWriter(filePath, true)){
                writer.write("[" + level + "] " + message + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
    }
}