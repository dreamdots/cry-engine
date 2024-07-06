package ru.adotsenko.engine.tinkoff.client;

import reactor.core.publisher.Flux;
import ru.tinkoff.piapi.contract.v1.*;

import java.time.Instant;

public interface TinkoffApi {
    Flux<Currency> getTradableCurrencies();

    Flux<Bond> getTradableBonds();

    Flux<Share> getTradableShares();

    Flux<Etf> getTradableEtfs();

    Flux<HistoricCandle> getCandles(String figi, Instant from, Instant to, CandleInterval interval);
}
