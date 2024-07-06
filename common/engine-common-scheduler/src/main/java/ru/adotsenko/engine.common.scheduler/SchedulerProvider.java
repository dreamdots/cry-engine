package ru.adotsenko.engine.common.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SchedulerProvider {
    private final Map<String, Scheduler> schedulers = new ConcurrentHashMap<>();

    public SchedulerProvider(SchedulerProperties schedulerProperties) {
        if (!(schedulerProperties.getPools() == null)) {
            schedulerProperties.getPools().forEach((poolName, setting) -> schedulers.put(poolName, construct(poolName, setting)));
        }
    }

    private Scheduler construct(String name, SchedulerProperties.SchedulerSetting setting) {
        var poolSize = setting.getMinCount();
        if (setting.getPercentage() != 0) {
            var baseSize = setting.isParallel() ? Schedulers.DEFAULT_POOL_SIZE : Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE;
            poolSize = baseSize * setting.getPercentage() / 100;
            if (setting.getMinCount() != 0) {
                poolSize = Math.max(setting.getMinCount(), poolSize);
            }
        }

        log.info("Создан шедулер с параметрами minCount = {} percentage = {} isParallel = {}",
                poolSize, setting.getPercentage(), setting.isParallel()
        );

        return setting.isParallel()
                ? Schedulers.newParallel(name, poolSize)
                : Schedulers.newBoundedElastic(poolSize, Integer.MAX_VALUE, name);
    }

    public Scheduler get(String name) {
        return schedulers.get(name);
    }
}
