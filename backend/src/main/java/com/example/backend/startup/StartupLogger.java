package com.example.backend.startup;

import com.example.backend.util.logger.*;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    @PostConstruct
    public void logStartup() {
        LoggerManager.getInstance().logToAll(LogLevel.INFO, "App started successfully.");
    }
}
