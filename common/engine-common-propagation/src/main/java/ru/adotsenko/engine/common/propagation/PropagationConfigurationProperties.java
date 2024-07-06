package ru.adotsenko.engine.common.propagation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "propagation")
public class PropagationConfigurationProperties {
    private Map<WebFilter, WebFilterProperties> filters;

    @Data
    public static class WebFilterProperties {
        private Integer order;
        private Boolean enabled = false;
    }
}
