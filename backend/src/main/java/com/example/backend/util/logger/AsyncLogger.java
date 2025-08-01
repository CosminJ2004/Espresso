package com.example.backend.util.logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncLogger {

    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final Thread loggerThread;

    private volatile boolean running = true;

    public AsyncLogger() {

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

    public void log(String message) {

        logQueue.offer(message);
    }

    public void stop() {
        running = false;
        loggerThread.interrupt();
    }

    public static void main(String[] args) throws InterruptedException {
        AsyncLogger logger = new AsyncLogger();

        for (int i = 0; i < 10; i++) {
            logger.log("Mesajul " + i);
            Thread.sleep(500);
        }

        logger.stop();
    }
}
