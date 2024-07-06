package ru.adotsenko.engine.tinkoff.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Service
@Slf4j
@Profile("tinkoff")
public class TinkoffApiImpl implements TinkoffApi {
    private final Map<String, LongSummaryStatistics> requestStat = new ConcurrentHashMap<>();
    private final Scheduler tinkoffApiScheduler = Schedulers.newBoundedElastic(
            Short.MAX_VALUE,
            Short.MAX_VALUE,
            "tinkoff-api"
    );

    private final InvestApi investApi;
    private final TinkoffApiConfigurationProperties tinkoffApiConfigurationProperties;

    public TinkoffApiImpl(InvestApi investApi,
                          TinkoffApiConfigurationProperties tinkoffApiConfigurationProperties) {
        this.investApi = investApi;
        this.tinkoffApiConfigurationProperties = tinkoffApiConfigurationProperties;
    }

    private <T> Mono<T> wrapCall(Supplier<CompletableFuture<T>> apiCall, String methodName) {
        var startTime = new AtomicLong();
        return Mono.defer(() -> Mono.just(startTime))
                .publishOn(tinkoffApiScheduler)
                .flatMap(ignore -> Mono.fromFuture(apiCall))
                .doOnSubscribe(s -> startTime.set(System.currentTimeMillis()))
                .timeout(Duration.of(tinkoffApiConfigurationProperties.getApiTimeout(), ChronoUnit.MILLIS))
                .retryWhen(Retry.fixedDelay(tinkoffApiConfigurationProperties.getRetryCount(), Duration.ofMillis(tinkoffApiConfigurationProperties.getRetryInterval())))
                .onErrorResume(t -> {
                    log.warn("Ошибка при работе метода {} Тинькофф API", methodName, t);
                    return Mono.empty();
                })
                .doFinally(s -> logRequestTime(methodName, startTime));
    }

    private void logRequestTime(String methodName, AtomicLong startTime) {
        var processingTime = System.currentTimeMillis() - startTime.get();
        log.debug("Метод {} отработал за {} мс", methodName, processingTime);
        requestStat.compute(methodName, (k, v) -> {
            if (v == null) {
                v = new LongSummaryStatistics();
            }

            v.accept(processingTime);
            return v;
        });
    }

    @Override
    public Flux<Currency> getTradableCurrencies() {
        return this
                .wrapCall(() -> investApi.getInstrumentsService().getTradableCurrencies(), "getTradableCurrencies")
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Bond> getTradableBonds() {
        return this
                .wrapCall(() -> investApi.getInstrumentsService().getTradableBonds(), "getTradableBonds")
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Share> getTradableShares() {
        return this
                .wrapCall(() -> investApi.getInstrumentsService().getTradableShares(), "getTradableShares")
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Etf> getTradableEtfs() {
        return this
                .wrapCall(() -> investApi.getInstrumentsService().getTradableEtfs(), "getTradableEtfs")
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<HistoricCandle> getCandles(String figi, Instant from, Instant to, CandleInterval interval) {
        return this
                .wrapCall(
                        () -> investApi.getMarketDataService().getCandles(figi, from, to, interval),
                        "getCandles")
                .flatMapMany(Flux::fromIterable);
    }
}
