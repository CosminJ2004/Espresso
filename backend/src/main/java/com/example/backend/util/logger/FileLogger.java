package com.example.backend.util.logger;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileLogger extends BaseLogger {

    @Value("${logging.custom.file-path:./logs/app.log}")
    private String filePath;

    @Value("${logging.custom.file-max-size:10485760}") // 10MB default
    private long maxFileSize;

    @Value("${logging.custom.file-max-files:5}")
    private int maxFiles;

    private Path currentLogFile;

    public FileLogger(LogProcessor logProcessor) {
        super(logProcessor);
    }

    @PostConstruct
    public void initializeLogFile() {
        try {
            currentLogFile = Paths.get(filePath);
            Files.createDirectories(currentLogFile.getParent());
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }

    @Override
    public void writeLog(LogEntry entry) {
        try {
            String logLine = formatLogEntry(entry) + "\n";

            // Check if file rotation is needed
            if (needsRotation()) {
                rotateLogFile();
            }

            Files.write(currentLogFile, logLine.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
            // Fallback to console
            System.out.println(formatLogEntry(entry));
        }
    }

    private boolean needsRotation() {
        try {
            return Files.exists(currentLogFile) && Files.size(currentLogFile) > maxFileSize;
        } catch (IOException e) {
            return false;
        }
    }

    private void rotateLogFile() throws IOException {
        // Rotate existing files
        for (int i = maxFiles - 1; i > 0; i--) {
            Path oldFile = Paths.get(filePath + "." + i);
            Path newFile = Paths.get(filePath + "." + (i + 1));

            if (Files.exists(oldFile)) {
                if (i == maxFiles - 1) {
                    Files.deleteIfExists(newFile);
                }
                Files.move(oldFile, newFile);
            }
        }

        Path rotatedFile = Paths.get(filePath + ".1");
        Files.move(currentLogFile, rotatedFile);
    }
}