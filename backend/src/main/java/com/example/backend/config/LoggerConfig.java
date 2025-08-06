package com.example.backend.config;

import com.example.backend.util.logger.*;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    @PostConstruct
    public void setupLoggers() {
        LoggerManager manager = LoggerManager.getInstance();
        manager.addLogger("console", LoggerFactory.create(LoggerType.CONSOLE));
        manager.addLogger("file", LoggerFactory.create(LoggerType.FILE, "app.log"));

    }
}
