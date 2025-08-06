package com.example.backend.util.logger;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoggerManager {
    private final Map<String, ILogger> loggers = new HashMap<>();

    @Getter
    private static final LoggerManager instance = new LoggerManager();

    public void addLogger(String name, ILogger logger) {
        if (loggers.containsKey(name)) {
            throw new IllegalArgumentException("Logger with name '" + name + "' already exists.");
        }
        loggers.put(name, logger);
    }

    public void log(String loggerName, LogLevel level, String message) {
        ILogger logger = loggers.get(loggerName);
        if (logger == null) {
            throw new IllegalArgumentException("Logger '" + loggerName + "' not found.");
        }
        logger.log(level, message);
    }

    public void logToAll(LogLevel level, String message) {
        for  (ILogger logger : loggers.values()) {
            logger.log(level, message);
        }
    }

    public Set<String> getLoggerNames() {
        return loggers.keySet();
    }
}
