package ru.adotsenko.engine.notification.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.adotsenko")
public class EngineNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngineNotificationServiceApplication.class, args);
    }
}
