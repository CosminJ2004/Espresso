package com.example.backend.util.logger;

import org.springframework.stereotype.Service;

@Service
public class ConsoleLogger extends BaseLogger {

    public ConsoleLogger(LogProcessor logProcessor) {
        super(logProcessor);
    }

    @Override
    public void writeLog(LogEntry entry) {
        String logLine = formatLogEntry(entry);

        if ("ERROR".equals(entry.getLevel()) || "WARN".equals(entry.getLevel())) {
            System.err.println(logLine);
        } else {
            System.out.println(logLine);
        }
    }
}