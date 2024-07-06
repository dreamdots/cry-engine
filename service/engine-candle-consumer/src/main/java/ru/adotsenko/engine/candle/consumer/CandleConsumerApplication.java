package ru.adotsenko.engine.candle.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.adotsenko.engine.candle.consumer.service.CandleConsumerProperties;

@EnableConfigurationProperties(CandleConsumerProperties.class)
@SpringBootApplication(scanBasePackages = "ru.adotsenko")
public class CandleConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandleConsumerApplication.class, args);
    }
}
