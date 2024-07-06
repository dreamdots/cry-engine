package ru.adotsenko.engine.candle.processor.service.candle.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.adotsenko.engine.candle.processor.service.candle.CandleService;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;
import ru.adotsenko.engine.common.scheduler.SchedulerProvider;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Validated
@Service
public class CandleCacheServiceImpl implements CandleCacheService {
    private final CandleCacheProperties candleCacheProperties;
    private final Scheduler cacheScheduler;
    private final Cache<String, CandleCache> caches;
    private final CandleService candleService;

    public CandleCacheServiceImpl(CandleCacheProperties candleCacheProperties,
                                  @Lazy CandleService candleService,
                                  SchedulerProvider schedulerProvider) {
        this.candleCacheProperties = candleCacheProperties;

        this.caches = CacheBuilder.newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .initialCapacity(100)
                .expireAfterAccess(candleCacheProperties.getMetaCacheTtlHours(), TimeUnit.HOURS)
                .build();

        this.cacheScheduler = schedulerProvider.get("candle-cache-worker");
        this.candleService = candleService;
    }

    public static String buildCacheName(Candle candle) {
        return buildCacheName(candle.getFigi(), candle.getChannel(), candle.getInterval());
    }

    public static String buildCacheName(String figi, Channel channel, Interval interval) {
        return figi + channel + interval;
    }

    @Override
    public Flux<CandleCache> caches() {
        return Flux.fromIterable(caches.asMap().values());
    }

    @Override
    public Mono<Candle> put(Candle candle) {
        return getOrAddCache(candle)
                .publishOn(cacheScheduler)
                .flatMap(cache -> cache.put(candle));
    }

    @Override
    public Flux<Candle> putAll(Collection<Candle> candles) {
        if (candles.isEmpty()) {
            return Flux.empty();
        }

        var first = candles.stream().findAny().get();
        return getOrAddCache(first)
                .publishOn(cacheScheduler)
                .flatMapMany(cache -> cache.putAll(candles));
    }

    @Override
    public Flux<Candle> putAll(Candle... candles) {
        return putAll(List.of(candles));
    }

    @Override
    public Flux<Candle> getAll(String figi, Channel channel, Interval interval, int depth) {
        return getCache(figi, channel, interval)
                .publishOn(cacheScheduler)
                .flatMapMany(cache -> cache.getAll(depth));
    }

    @Override
    public Mono<Void> clear(String figi, Channel channel, Interval interval) {
        return getCache(figi, channel, interval)
                .publishOn(cacheScheduler)
                .flatMap(CandleCache::clear);
    }

    @Override
    public Mono<Candle> getLast(String figi, Channel channel, Interval interval) {
        return getCache(figi, channel, interval)
                .publishOn(cacheScheduler)
                .flatMap(CandleCache::getLast);
    }

    @Override
    public Mono<Candle> get(String figi, Channel channel, Interval interval, Instant key) {
        return getCache(figi, channel, interval)
                .publishOn(cacheScheduler)
                .flatMap(cache -> cache.get(key));
    }

    @Override
    public Mono<Integer> size(String figi, Channel channel, Interval interval) {
        return getCache(figi, channel, interval)
                .publishOn(cacheScheduler)
                .map(CandleCache::size)
                .doOnNext(size -> {
                    if (log.isDebugEnabled()) {
                        var cacheName = buildCacheName(figi, channel, interval);
                        log.debug("Текущий размер кэша {} - {}", cacheName, size);
                    }
                });
    }

    public Mono<CandleCache> getOrAddCache(Candle candle) {
        return getOrAddCache(candle.getFigi(), candle.getChannel(), candle.getInterval());
    }

    public Mono<CandleCache> getOrAddCache(String figi, Channel channel, Interval interval) {
        return Mono.fromSupplier(() -> {
            var cacheName = buildCacheName(figi, channel, interval);
            return caches.asMap().compute(cacheName, (name, cache) -> {
                if (cache == null) {
                    cache = new InMemoryCandleCache(cacheName, candleCacheProperties.getMaxSize());

                    loadCache(figi, interval, channel, name, cache);
                }

                return cache;
            });
        });
    }

    private void loadCache(String figi,
                           Interval interval,
                           Channel channel,
                           String name,
                           CandleCache cache) {
        var start = new AtomicLong();
        candleService
                .findAll(figi, channel, interval, candleCacheProperties.getMaxSize())
                .flatMap(cache::put)
                .doOnSubscribe(s -> start.set(System.currentTimeMillis()))
                .subscribeOn(cacheScheduler)
                .count()
                .doOnSuccess(added -> log.info(
                        "В кэш {} добавлено {} значений. Затрачено {} мс",
                        name, added,
                        System.currentTimeMillis() - start.get())
                )
                .subscribe();
    }

    public Mono<CandleCache> getCache(Candle candle) {
        return getCache(candle.getFigi(), candle.getChannel(), candle.getInterval());
    }

    public Mono<CandleCache> getCache(String figi, Channel channel, Interval interval) {
        return Mono.fromSupplier(() -> {
            var cacheName = buildCacheName(figi, channel, interval);

            var cache = caches.asMap().get(cacheName);
            if (cache == null) {
                log.debug("Кэш {} не найден", cacheName);
            }

            return cache;
        });
    }
}
