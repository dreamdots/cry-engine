package ru.adotsenko.engine.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "ru.adotsenko")
public class EngineGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngineGatewayApplication.class, args);
    }

}
