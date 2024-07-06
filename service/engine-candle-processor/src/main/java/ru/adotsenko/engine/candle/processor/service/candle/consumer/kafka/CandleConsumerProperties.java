package ru.adotsenko.engine.candle.processor.service.candle.consumer.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile("kafka")
@Data
@ConfigurationProperties("engine.candle-consumer")
public class CandleConsumerProperties {
    private Long delayMs;
    private String candleTopic;
}
