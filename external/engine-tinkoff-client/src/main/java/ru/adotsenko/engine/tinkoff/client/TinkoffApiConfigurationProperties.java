package ru.adotsenko.engine.tinkoff.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinkoff")
public class TinkoffApiConfigurationProperties {
    private Boolean isSandBoxMode;
    private Integer retryCount = 0;
    private String authToken;
    private Long apiTimeout = 5000L;
    private Long retryInterval = 20L;
}
