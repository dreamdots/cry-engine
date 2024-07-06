package ru.adotsenko.engine.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderOptions;
import reactor.util.function.Tuples;
import ru.adotsenko.engine.common.model.entity.Event;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class KafkaProducer {
    private final KafkaProperties properties;
    private final ObjectMapper mapper;
    private final ReactiveKafkaProducerTemplate<String, String> senderTemplate;

    @SuppressWarnings("removal")
    public KafkaProducer(KafkaProperties properties,
                         ObjectMapper mapper) {
        this(properties, mapper, new ReactiveKafkaProducerTemplate<>(SenderOptions.create(properties.buildProducerProperties())));
    }

    public KafkaProducer(KafkaProperties properties,
                         ObjectMapper mapper,
                         ReactiveKafkaProducerTemplate<String, String> senderTemplate) {
        this.properties = properties;
        this.mapper = mapper;
        this.senderTemplate = senderTemplate;
    }

    public <T> Mono<Void> send(String topicName, T data) {
        return send(topicName, null, data);
    }

    public <T> Mono<Void> send(String topicName, T data, Headers headers) {
        return send(topicName, null, data, headers);
    }

    public <T> Mono<Void> send(String topicName, String key, T data) {
        return send(topicName, key, data, new RecordHeaders());
    }

    public <T> Mono<Void> send(String topicName, String key, T data, Headers headers) {
        return convertDataToString(data)
                .flatMap(jsonData -> doSend(
                        topicName,
                        key,
                        jsonData,
                        headers,
                        data.getClass().getCanonicalName()));
    }

    public Mono<Void> send(String topicName, String key, String data, Headers headers, String clazz) {
        return doSend(topicName, key, data, headers, clazz);
    }

    private Mono<Void> doSend(String topicName, String key, String data, Headers headers, String clazz) {
        var messageId = UUID.randomUUID().toString();
        return Mono.just(data)
                .flatMap(payload -> buildProducerRecord(topicName, messageId, key, payload, clazz, headers))
                .flatMap(record -> senderTemplate.send(record).map(result -> Tuples.of(record, result)))
                .doOnSuccess(tuple -> log.debug("Сообщение отправлено в топик {}, type: {}, payload: {} offset : {} partition: {}",
                        topicName, clazz, data, tuple.getT2().recordMetadata().offset(), tuple.getT2().recordMetadata().partition()
                ))
                .doOnError(err -> log.error("Ошибка отправки сообщения в топик {}, type: {} payload: {}",
                        topicName, clazz, data, err
                ))
                .then();
    }

    private <T> Mono<String> convertDataToString(T data) {
        try {
            return Mono.just(mapper.writeValueAsString(Event.of(data)));
        } catch (final Exception e) {
            return Mono.error(e);
        }
    }

    private Mono<ProducerRecord<String, String>> buildProducerRecord(String topicName,
                                                                     String messageId,
                                                                     String key,
                                                                     String payload,
                                                                     String payloadClass,
                                                                     Headers headers) {
        List<Header> allHeaders = new ArrayList<>(List.of(
                new RecordHeader("messageId", messageId.getBytes()),
                new RecordHeader(KafkaProducerConfiguration.HEADER_PAYLOAD_TYPE, payloadClass.getBytes()))
        );

        headers.forEach(allHeaders::add);

        return Mono.just(new ProducerRecord<>(topicName, null, Instant.now().toEpochMilli(), key, payload, allHeaders));
    }
}
