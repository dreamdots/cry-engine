package ru.adotsenko.engine.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("kafka")
public class KafkaProducerConfiguration {
    public static String HEADER_PAYLOAD_TYPE = "PayloadType";

    @Bean
    public KafkaProducer kafkaProducer(KafkaProperties kafkaProperties,
                                       ObjectMapper kafkaObjectMapper) {
        return new KafkaProducer(kafkaProperties, kafkaObjectMapper);
    }
}
