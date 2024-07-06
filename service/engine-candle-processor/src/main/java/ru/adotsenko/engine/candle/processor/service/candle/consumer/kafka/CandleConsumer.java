package ru.adotsenko.engine.candle.processor.service.candle.consumer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.candle.processor.service.candle.CandleService;
import ru.adotsenko.engine.candle.processor.service.candle.cache.CandleCacheService;
import ru.adotsenko.engine.common.kafka.KafkaConsumer;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.notification.service.api.NotificationApi;

@Component
@Slf4j
@Profile("kafka")
public class CandleConsumer extends KafkaConsumer<Candle> {
    private final CandleService candleService;
    private final CandleCacheService candleCacheService;
    private final NotificationApi notificationApi;

    public CandleConsumer(CandleConsumerProperties candleConsumerProperties,
                          KafkaProperties kafkaProperties,
                          @Qualifier("kafkaObjectMapper") ObjectMapper mapper,
                          CandleService candleService,
                          CandleCacheService candleCacheService,
                          NotificationApi notificationApi
    ) {
        super(
                candleConsumerProperties.getCandleTopic(),
                Candle.class, kafkaProperties,
                candleConsumerProperties.getDelayMs(),
                mapper
        );

        this.candleService = candleService;
        this.candleCacheService = candleCacheService;
        this.notificationApi = notificationApi;
    }

    @Override
    protected Mono<Void> onMessage(Candle message) {
        return candleService.save(message)
                .onErrorContinue((t, o) -> log.warn("Ошибка в процессе обработки свечи {}", message, t))
                .then();
    }
}
