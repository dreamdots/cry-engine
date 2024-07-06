package ru.adotsenko.engine.candle.processor.service.candle.cache;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;

import java.time.Instant;
import java.util.Collection;

public interface CandleCacheService {
    Flux<CandleCache> caches();

    Mono<Candle> put(Candle candle);

    Flux<Candle> putAll(Collection<Candle> candles);

    Flux<Candle> putAll(Candle... candles);

    default Flux<Candle> getAll(Candle candle, int depth) {
        return getAll(candle.getFigi(), candle.getChannel(), candle.getInterval(), depth);
    }

    Flux<Candle> getAll(String figi, Channel channel, Interval interval, int depth);

    default Mono<Void> clear(Candle candle) {
        return clear(candle.getFigi(), candle.getChannel(), candle.getInterval());
    }

    Mono<Void> clear(String figi, Channel channel, Interval interval);

    default Mono<Candle> getLast(Candle candle) {
        return getLast(candle.getFigi(), candle.getChannel(), candle.getInterval());
    }

    Mono<Candle> getLast(String figi, Channel channel, Interval interval);

    default Mono<Candle> get(Candle candle, Instant key) {
        return get(candle.getFigi(), candle.getChannel(), candle.getInterval(), key);
    }

    Mono<Candle> get(String figi, Channel channel, Interval interval, Instant key);

    default Mono<Integer> size(Candle candle) {
        return size(candle.getFigi(), candle.getChannel(), candle.getInterval());
    }

    Mono<Integer> size(String figi, Channel channel, Interval interval);
}
