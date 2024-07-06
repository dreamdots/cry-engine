package ru.adotsenko.engine.candle.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import ru.adotsenko.engine.candle.processor.service.candle.cache.CandleCacheProperties;
import ru.adotsenko.engine.candle.processor.service.candle.consumer.kafka.CandleConsumerProperties;
import ru.adotsenko.engine.notification.service.api.NotificationApi;

@EnableConfigurationProperties({
        CandleConsumerProperties.class,
        CandleCacheProperties.class
})
@EnableReactiveFeignClients(clients = {
        NotificationApi.class
})
@SpringBootApplication(scanBasePackages = "ru.adotsenko")
public class CandleProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandleProcessorApplication.class, args);
    }
}
