package com.example.backend.util.logger;

import lombok.Getter;

public class LogEntry {
    @Getter
    private final long timestamp;
    @Getter
    private final String level;
    @Getter
    private final String message;
    @Getter
    private final Object data;

    public LogEntry(String level, String message, Object data) {
        this.timestamp = System.currentTimeMillis();
        this.level = level;
        this.message = message;
        this.data = data;
    }
}