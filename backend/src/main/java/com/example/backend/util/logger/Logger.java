package com.example.backend.util.logger;

public interface Logger {
    void debug(String message);
    void debug(String message, Object data);
    void info(String message);
    void info(String message, Object data);
    void warn(String message);
    void warn(String message, Object data);
    void error(String message);
    void error(String message, Object data);
}
