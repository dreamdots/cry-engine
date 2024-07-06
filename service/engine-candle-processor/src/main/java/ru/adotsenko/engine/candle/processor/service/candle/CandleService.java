package ru.adotsenko.engine.candle.processor.service.candle;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;

import java.time.Instant;
import java.util.Collection;

public interface CandleService {
    Flux<Candle> findAll(String figi, Channel channel, Interval interval, int depth);

    Mono<Candle> save(@Valid Candle candle);

    Flux<Candle> saveAll(Collection<Candle> candles);

    Mono<Candle> find(String figi, Channel channel, Interval interval, Instant dateTime);

    Mono<Candle> findFirst(String figi, Channel channel, Interval interval);

    Mono<Candle> findLast(String figi, Channel channel, Interval interval);
}
