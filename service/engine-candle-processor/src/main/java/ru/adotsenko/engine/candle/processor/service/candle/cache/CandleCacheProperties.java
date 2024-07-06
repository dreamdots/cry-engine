package ru.adotsenko.engine.candle.processor.service.candle.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("engine.candle-cache")
public class CandleCacheProperties {
    private Integer maxSize = 5000;
    private Integer metaCacheTtlHours = 24;
}
