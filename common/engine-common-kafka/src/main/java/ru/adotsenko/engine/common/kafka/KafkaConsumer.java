package ru.adotsenko.engine.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import ru.adotsenko.engine.common.model.entity.Event;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class KafkaConsumer<R> {
    private final Scheduler consumerScheduler = Schedulers.newParallel(
            "kafka-consumer",
            Runtime.getRuntime().availableProcessors()
    );
    private final Class<R> baseType;
    private final KafkaProperties kafkaProperties;
    private final ObjectMapper mapper;

    public KafkaConsumer(String topicName,
                         Class<R> baseType,
                         KafkaProperties kafkaProperties,
                         ObjectMapper mapper) {
        this(Collections.singletonList(topicName), baseType, kafkaProperties, null, mapper);
    }

    public KafkaConsumer(String topicName,
                         Class<R> baseType,
                         KafkaProperties kafkaProperties,
                         Long delayMs,
                         ObjectMapper mapper) {
        this(Collections.singletonList(topicName), baseType, kafkaProperties, delayMs, mapper);
    }


    public KafkaConsumer(List<String> topicNames,
                         Class<R> baseType,
                         KafkaProperties kafkaProperties,
                         Long delayMs,
                         ObjectMapper mapper) {
        this.mapper = mapper;
        this.kafkaProperties = kafkaProperties;
        this.baseType = baseType;

        var consumerOptions = ReceiverOptions
                .create(kafkaProperties.buildConsumerProperties())
                .subscription(topicNames);

        KafkaReceiver.create(consumerOptions)
                .receive()
                .groupBy(r -> r.receiverOffset().topicPartition())
                .flatMap(group -> group.publishOn(consumerScheduler)
                        .concatMap(r -> this.doProcessRecord(r, delayMs)))
                .subscribe();
    }

    protected abstract Mono<Void> onMessage(R message);

    private Mono<Void> doProcessRecord(ReceiverRecord<Object, Object> record,
                                       Long delayMs) {
        return Optional.ofNullable(delayMs)
                .map(d -> processRecord(record).delaySubscription(Duration.ofMillis(d)))
                .orElse(processRecord(record))
                .onErrorResume(err -> Mono.fromRunnable(() -> log.error("Ошибка в процессе обработки сообщения", err)));
    }

    private Mono<Void> processRecord(ReceiverRecord<Object, Object> record) {
        return readMessage(record)
                .flatMap(tuple -> {
                    if (tuple.getT2() instanceof UnknownMessage) {
                        log.warn("Получено сообщение неизвестного типа {}", tuple.getT1());
                        return Mono.empty();
                    } else {
                        final R data = ((Event<R>) tuple.getT2()).getData();

                        log.debug("Получено сообщение type: {} payload: {}, offset = {}, timestamp = {} from partition = {}",
                                data.getClass().getSimpleName(), tuple.getT1(), record.offset(), record.timestamp(), record.partition()
                        );

                        return onMessage(data);
                    }
                });
    }

    private Mono<Tuple2<String, Object>> readMessage(ReceiverRecord<Object, Object> record) {
        return Mono.just(record.value())
                .map(Object::toString)
                .flatMap(msg -> getPayloadClass(record)
                        .flatMap(payloadClass -> convertMessageToDTO(msg, payloadClass))
                        .map(dto -> Tuples.of(msg, dto))
                        .onErrorResume(ClassNotFoundException.class, err -> Mono.just(Tuples.of(msg, new UnknownMessage())))
                );
    }

    private Mono<Object> convertMessageToDTO(String message, Class<?> payloadClass) {
        try {
            return Mono.just(mapper.readValue(
                    message,
                    TypeFactory.defaultInstance().constructParametricType(Event.class, payloadClass)
            ));
        } catch (final Exception e) {
            log.error("Ошибка в процессе обработки сообщения", e);
            return Mono.error(new RuntimeException(e));
        }
    }

    private Mono<Class<?>> getPayloadClass(ReceiverRecord<Object, Object> record) {
        return Optional.ofNullable(record.headers())
                .map(headers -> headers.lastHeader(KafkaProducerConfiguration.HEADER_PAYLOAD_TYPE))
                .map(header -> ReflectionUtils.getClassForName(new String(header.value())))
                .orElse(Mono.just(baseType));
    }

    // для индикации неизвестных типов сообщений
    private static class UnknownMessage {
    }
}
