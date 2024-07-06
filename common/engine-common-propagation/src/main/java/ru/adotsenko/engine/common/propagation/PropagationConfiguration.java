package ru.adotsenko.engine.common.propagation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PropagationConfigurationProperties.class)
public class PropagationConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "propagation.filters.trace-id", name = "enabled", havingValue = "true")
    public TraceIdHeaderEnricher traceIdHeaderEnricher() {
        return new TraceIdHeaderEnricher();
    }

    @Bean
    @ConditionalOnProperty(prefix = "propagation.filters.logging", name = "enabled", havingValue = "true")
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
}
