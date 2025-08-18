package com.example.backend.util.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class LoggerService implements Logger {

    private final ConsoleLogger consoleLogger;
    private final FileLogger fileLogger;

    @Autowired
    public LoggerService(ConsoleLogger consoleLogger, FileLogger fileLogger) {
        this.consoleLogger = consoleLogger;
        this.fileLogger = fileLogger;
    }

    @Override
    public void debug(String message) {
        consoleLogger.debug(message);
        fileLogger.debug(message);
    }

    @Override
    public void debug(String message, Object data) {
        consoleLogger.debug(message, data);
        fileLogger.debug(message, data);
    }

    @Override
    public void info(String message) {
        consoleLogger.info(message);
        fileLogger.info(message);
    }

    @Override
    public void info(String message, Object data) {
        consoleLogger.info(message, data);
        fileLogger.info(message, data);
    }

    @Override
    public void warn(String message) {
        consoleLogger.warn(message);
        fileLogger.warn(message);
    }

    @Override
    public void warn(String message, Object data) {
        consoleLogger.warn(message, data);
        fileLogger.warn(message, data);
    }

    @Override
    public void error(String message) {
        consoleLogger.error(message);
        fileLogger.error(message);
    }

    @Override
    public void error(String message, Object data) {
        consoleLogger.error(message, data);
        fileLogger.error(message, data);
    }
}