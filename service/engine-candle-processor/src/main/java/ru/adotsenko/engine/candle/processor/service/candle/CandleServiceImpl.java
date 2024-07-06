package ru.adotsenko.engine.candle.processor.service.candle;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.adotsenko.engine.candle.processor.service.candle.cache.CandleCacheService;
import ru.adotsenko.engine.candle.processor.service.repository.CandleRepository;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;
import ru.adotsenko.engine.common.scheduler.SchedulerProvider;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Validated
@Service
public class CandleServiceImpl implements CandleService {
    private final CandleCacheService candleCacheService;
    private final CandleService candleService;
    private final CandleRepository candleRepository;
    private final Scheduler processorScheduler;

    public CandleServiceImpl(CandleCacheService candleCacheService,
                             @Lazy CandleService candleService,
                             CandleRepository candleRepository,
                             SchedulerProvider schedulerProvider) {
        this.candleCacheService = candleCacheService;
        this.candleService = candleService;
        this.candleRepository = candleRepository;
        this.processorScheduler = schedulerProvider.get("candle-processor-worker");
    }

    @Override
    public Flux<Candle> findAll(String figi, Channel channel, Interval interval, int depth) {
        return candleRepository
                .findAllByFigiAndChannelAndIntervalOrderByDateTime(figi, channel, interval)
                .takeLast(depth)
                .subscribeOn(processorScheduler);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Candle> save(@Valid Candle candle) {
        return candleCacheService.get(candle, candle.getDateTime())
                .publishOn(processorScheduler)
                .flatMap(fromCache -> {
                    if (fromCache.getDateTime().compareTo(candle.getDateTime()) > 0) {
                        log.debug("Свеча c временем {} не будет добавлена, т.к. последняя свеча позднее {}",
                                candle.getDateTime(), fromCache.getDateTime()
                        );

                        return Mono.empty();
                    } else {
                        fromCache.setLowestPrice(candle.getLowestPrice());
                        fromCache.setClosingPrice(candle.getClosingPrice());
                        fromCache.setOpenPrice(candle.getOpenPrice());
                        fromCache.setHighestPrice(candle.getHighestPrice());

                        return doSave(fromCache);
                    }
                })
                .switchIfEmpty(doSave(candle))
                .onErrorResume(OptimisticLockingFailureException.class, t -> candleService.save(candle));
    }

    private Mono<Candle> doSave(Candle candle) {
        return candleRepository
                .save(candle)
                .doOnNext(c -> log.debug("В бд добавлена свеча {}", c))
                .flatMap(candleCacheService::put);
    }

    @Override
    @Transactional
    public Flux<Candle> saveAll(Collection<Candle> candles) {
        return Flux.fromIterable(candles)
                .publishOn(processorScheduler)
                .groupBy(c -> c.getFigi() + c.getInterval() + c.getChannel())
                .flatMap(group -> group
                        .sort(Comparator.comparing(Candle::getDateTime).reversed())
                        .flatMap(candleService::save, 1)
                );
    }

    @Override
    public Mono<Candle> find(String figi, Channel channel, Interval interval, Instant dateTime) {
        return candleRepository
                .findFirstByFigiAndDateTimeAndIntervalAndChannel(figi, dateTime, interval, channel)
                .subscribeOn(processorScheduler);
    }

    @Override
    public Mono<Candle> findFirst(String figi, Channel channel, Interval interval) {
        return candleRepository
                .findFirstByFigiAndChannelAndIntervalOrderByDateTime(figi, channel, interval)
                .subscribeOn(processorScheduler);
    }

    @Override
    public Mono<Candle> findLast(String figi, Channel channel, Interval interval) {
        return candleRepository
                .findFirstByFigiAndChannelAndIntervalOrderByDateTimeDesc(figi, channel, interval)
                .subscribeOn(processorScheduler);
    }
}
