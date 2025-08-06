package com.example.backend.util.logger;

public class Log {


    public static void log(String loggerName,LogLevel level, String message) {
        LoggerManager.getInstance().log(loggerName, level, message);
    }
}
