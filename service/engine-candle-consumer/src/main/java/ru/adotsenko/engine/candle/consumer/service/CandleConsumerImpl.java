package ru.adotsenko.engine.candle.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.adotsenko.engine.candle.consumer.mapper.CandleMapper;
import ru.adotsenko.engine.common.kafka.KafkaProducer;
import ru.adotsenko.engine.common.scheduler.SchedulerProvider;
import ru.adotsenko.engine.tinkoff.client.TinkoffStreamApi;

@Component
@Slf4j
public class CandleConsumerImpl implements CandleConsumer {
    private final TinkoffStreamApi tinkoffStreamApi;
    private final CandleConsumerProperties candleConsumerProperties;
    private final CandleMapper candleMapper;
    private final Scheduler processorScheduler;
    private final KafkaProducer kafkaProducer;

    public CandleConsumerImpl(TinkoffStreamApi tinkoffStreamApi,
                              CandleConsumerProperties candleConsumerProperties,
                              CandleMapper candleMapper,
                              SchedulerProvider schedulerProvider,
                              KafkaProducer kafkaProducer) {
        this.tinkoffStreamApi = tinkoffStreamApi;
        this.candleConsumerProperties = candleConsumerProperties;
        this.candleMapper = candleMapper;
        this.processorScheduler = schedulerProvider.get("candle-processor-worker");
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void consumeCandles() {
        candleConsumerProperties.getStreams().forEach((streamId, subscription) -> {
            tinkoffStreamApi.subscribeToCandlesStream(subscription.getFigis(), streamId, response -> {
                Mono.fromSupplier(() -> candleMapper.map(response.getCandle()))
                        .filter(c -> StringUtils.hasLength(c.getFigi()) && c.getInterval() != null && c.getChannel() != null)
                        .doOnNext(candle -> log.debug("Получена новая свеча {}", candle))
                        .flatMap(candle -> kafkaProducer.send(
                                candleConsumerProperties.getCandleTopic(),
                                candle.getFigi() + candle.getInterval() + candle.getChannel(),
                                candle
                        ))
                        .subscribeOn(processorScheduler)
                        .subscribe();
            });
        });
    }
}
