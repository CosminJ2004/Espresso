package com.example.backend.util.logger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class LogProcessor {

    private final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Getter
    private volatile boolean running = true;

    @Setter
    private LogWriter logWriter;

    @PostConstruct
    public void start() {
        if (logWriter != null) {
            startProcessor();
        }
    }

    @PreDestroy
    public void shutdown() {
        running = false;

        // Process remaining logs
        while (!logQueue.isEmpty()) {
            try {
                LogEntry entry = logQueue.poll();
                if (entry != null && logWriter != null) {
                    logWriter.writeLog(entry);
                }
            } catch (Exception e) {
                System.err.println("Error flushing log: " + e.getMessage());
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public void addLog(String level, String message, Object data) {
        try {
            logQueue.offer(new LogEntry(level, message, data));
        } catch (Exception e) {
            System.err.println("Failed to queue log entry: " + e.getMessage());
        }
    }

    public int getQueueSize() {
        return logQueue.size();
    }

    private void startProcessor() {
        executor.submit(() -> {
            while (running || !logQueue.isEmpty()) {
                try {
                    LogEntry entry = logQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (entry != null && logWriter != null) {
                        logWriter.writeLog(entry);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error processing log: " + e.getMessage());
                }
            }
        });
    }
}