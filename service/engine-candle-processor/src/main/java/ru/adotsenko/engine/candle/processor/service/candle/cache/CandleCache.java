package ru.adotsenko.engine.candle.processor.service.candle.cache;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Candle;

import java.time.Instant;
import java.util.Collection;

public interface CandleCache {
    String getName();

    int getMaxSize();

    Mono<Candle> put(Candle candle);

    Flux<Candle> putAll(Collection<Candle> candles);

    Flux<Candle> putAll(Candle... candles);

    Flux<Candle> getAll(int depth);

    Mono<Void> clear();

    Mono<Candle> getLast();

    Mono<Candle> get(Instant key);

    int size();
}
