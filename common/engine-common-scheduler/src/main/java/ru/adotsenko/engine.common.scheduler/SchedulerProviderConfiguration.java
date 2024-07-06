package ru.adotsenko.engine.common.scheduler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
public class SchedulerProviderConfiguration {
}
