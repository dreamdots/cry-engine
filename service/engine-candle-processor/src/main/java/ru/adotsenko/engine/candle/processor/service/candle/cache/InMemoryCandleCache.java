package ru.adotsenko.engine.candle.processor.service.candle.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Candle;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
public class InMemoryCandleCache implements CandleCache {
    @Getter(onMethod_ = @Override)
    private final String name;
    @Getter(onMethod_ = @Override)
    private final int maxSize;
    private final ConcurrentSkipListMap<Instant, Candle> cache;

    public InMemoryCandleCache(String name, int maxSize) {
        this.maxSize = maxSize;
        this.name = name;
        this.cache = new ConcurrentSkipListMap<>();
        log.debug("Создан кэш {} с размерностью {}", name, maxSize);
    }

    @Override
    public Mono<Candle> put(Candle candle) {
        return Mono.fromSupplier(() -> checkAndPut(candle));
    }

    private Candle checkAndPut(Candle candle) {
        if (cache.size() >= maxSize) {
            var firstEntry = cache.firstEntry();
            if (firstEntry.getKey().compareTo(candle.getDateTime()) > 0) {
                log.debug("Свеча c временем {} не будет добавлена, т.к. первая свеча в кэше позднее {}",
                        candle.getDateTime(), firstEntry.getKey()
                );

                return firstEntry.getValue();
            } else {
                log.debug("Из кэша будет удалена свеча {}", firstEntry.getValue());
                cache.pollFirstEntry();
            }
        }

        cache.put(candle.getDateTime(), candle);
        log.debug("В кэш {} добавлена свеча {}", name, candle);
        return candle;
    }

    @Override
    public Flux<Candle> putAll(Collection<Candle> candles) {
        return Flux.fromIterable(candles)
                .flatMap(this::put);
    }

    @Override
    public Flux<Candle> putAll(Candle... candles) {
        return Flux.fromArray(candles)
                .flatMap(this::put);
    }

    @Override
    public Flux<Candle> getAll(int depth) {
        if (depth >= maxSize) {
            depth = maxSize;
        }

        var values = cache.values();
        log.debug("Найдено {} свечей в кэше {}", values.size(), name);
        return values.size() > depth
                ? Flux.fromIterable(values).takeLast(depth)
                : Flux.fromIterable(values);
    }

    @Override
    public Mono<Void> clear() {
        return Mono.fromRunnable(cache::clear)
                .doOnSuccess(v -> log.debug("Кэш {} был очищен", name))
                .then();
    }

    @Override
    public Mono<Candle> getLast() {
        return Mono.fromSupplier(() -> {
            log.debug("Поиск последней свечи в кэше {}", name);
            if (cache.isEmpty()) {
                log.debug("В кэше {} отсутствуют свечи", name);
                return null;
            } else {
                var last = cache.lastEntry().getValue();
                log.debug("В кэше {} найдена самая актуальная свеча {}", name, last);
                return last;
            }
        });
    }

    @Override
    public Mono<Candle> get(Instant key) {
        return Mono.fromSupplier(() -> {
            log.debug("Поиск свечи в кэше {} по ключу {}", name, key);

            var value = cache.get(key);
            if (value == null) {
                log.debug("В кэше {} нет значения с ключом {}", name, key);
            } else {
                log.debug("В кэше {} по ключу {} найдена свеча {}", name, key, value);
            }

            return value;
        });
    }

    @Override
    public int size() {
        return cache.size();
    }
}
