package com.example.backend.util.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ConsoleLogger implements ILogger {
    private final LogLevel minLevel;
    private final DateTimeFormatter formatter;

    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final Thread loggerThread;

    private volatile boolean running = true;


    public ConsoleLogger(LogLevel minLevel) {
        this.minLevel = minLevel;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        loggerThread = new Thread(() -> {
            while (running || !logQueue.isEmpty()) {
                try {
                    String message = logQueue.take();
                    System.out.println("[LOG] " + message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        loggerThread.setDaemon(true);
        loggerThread.start();
    }

    @Override
    public void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = timestamp + " - " + "[" + level + "] " + message + "\n";

        if (level.ordinal() >= minLevel.ordinal()) {
//            System.out.println(logEntry);
            logQueue.offer(logEntry);
        }
    }
}