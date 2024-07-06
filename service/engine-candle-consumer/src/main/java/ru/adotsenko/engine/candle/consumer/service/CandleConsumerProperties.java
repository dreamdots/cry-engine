package ru.adotsenko.engine.candle.consumer.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties("engine.candle-consumer")
public class CandleConsumerProperties {
    private Map<String, CandleListenerSubscription> streams;
    private String candleTopic;

    @Data
    public static class CandleListenerSubscription {
        private Boolean enabled;
        private List<String> figis;
    }
}
