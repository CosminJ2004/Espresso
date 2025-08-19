package com.example.backend.util.logger;

import java.time.Instant;

public abstract class BaseLogger implements Logger, LogWriter {

    protected final LogProcessor logProcessor;

    protected BaseLogger(LogProcessor logProcessor) {
        this.logProcessor = logProcessor;
        this.logProcessor.setLogWriter(this);
    }

    @Override
    public void debug(String message) { logProcessor.addLog("DEBUG", message, null); }
    @Override
    public void debug(String message, Object data) { logProcessor.addLog("DEBUG", message, data); }
    @Override
    public void info(String message) { logProcessor.addLog("INFO", message, null); }
    @Override
    public void info(String message, Object data) { logProcessor.addLog("INFO", message, data); }
    @Override
    public void warn(String message) { logProcessor.addLog("WARN", message, null); }
    @Override
    public void warn(String message, Object data) { logProcessor.addLog("WARN", message, data); }
    @Override
    public void error(String message) { logProcessor.addLog("ERROR", message, null); }
    @Override
    public void error(String message, Object data) { logProcessor.addLog("ERROR", message, data); }

    protected String formatLogEntry(LogEntry entry) {
        String timestamp = Instant.ofEpochMilli(entry.getTimestamp()).toString();
        String logLine = String.format("[%s] %s: %s", timestamp, entry.getLevel(), entry.getMessage());

        if (entry.getData() != null) {
            logLine += " | Data: " + entry.getData();
        }

        return logLine;
    }

    // Abstract method for subclasses to implement
    @Override
    public abstract void writeLog(LogEntry entry);
}