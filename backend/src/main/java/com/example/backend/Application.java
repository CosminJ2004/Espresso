package com.example.backend;

import com.example.backend.config.DotenvInitializer;
import com.example.backend.util.logger.LogLevel;
import com.example.backend.util.logger.LoggerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.addInitializers(new DotenvInitializer());
		app.run(args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logStartup() {
		LoggerManager.getInstance().logToAll(LogLevel.INFO, "App started successfully.");
	}
}
