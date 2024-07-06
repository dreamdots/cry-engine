package ru.adotsenko.engine.common.kafka;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class ReflectionUtils {

    public static Mono<Class<?>> getClassForName(String className) {
        try {
            return Mono.just(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Mono.error(e);
        }
    }
}
