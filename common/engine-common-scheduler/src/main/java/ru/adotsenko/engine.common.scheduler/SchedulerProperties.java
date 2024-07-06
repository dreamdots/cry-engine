package ru.adotsenko.engine.common.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("engine.scheduler")
public class SchedulerProperties {
    private Map<String, SchedulerSetting> pools;

    @Data
    @AllArgsConstructor
    public static class SchedulerSetting {
        private Integer minCount;
        private int percentage;
        private boolean parallel;
    }
}
