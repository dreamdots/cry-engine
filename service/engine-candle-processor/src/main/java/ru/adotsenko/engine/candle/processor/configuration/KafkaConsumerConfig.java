package ru.adotsenko.engine.candle.processor.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;
import ru.adotsenko.engine.candle.processor.service.candle.consumer.kafka.CandleConsumerProperties;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Event;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final CandleConsumerProperties candleConsumerProperties;

    @Bean
    public ReceiverOptions<String, Event<Candle>> candleReceiverOptions(KafkaProperties kafkaProperties) {
        ReceiverOptions<String, Event<Candle>> opt = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());

        opt = opt
                .withKeyDeserializer(new StringDeserializer())
                .withValueDeserializer(new JsonDeserializer<>(
                        new TypeReference<Event<Candle>>() {
                        },
                        JsonMapper.builder()
                                .addModule(new ParameterNamesModule())
                                .addModule(new Jdk8Module())
                                .addModule(new JavaTimeModule())
                                .build())
                );

        return opt.subscription(Collections.singletonList(candleConsumerProperties.getCandleTopic()));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, Event<Candle>> candleConsumerTemplate(ReceiverOptions<String, Event<Candle>> candleReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(candleReceiverOptions);
    }
}
