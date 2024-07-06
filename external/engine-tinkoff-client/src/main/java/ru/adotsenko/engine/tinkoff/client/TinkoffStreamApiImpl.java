package ru.adotsenko.engine.tinkoff.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Profile("tinkoff")
@Slf4j
@RequiredArgsConstructor
public class TinkoffStreamApiImpl implements TinkoffStreamApi {
    private final InvestApi investApi;

    @Override
    public void subscribeToCandlesStream(Collection<String> figis,
                                         String streamId,
                                         StreamProcessor<MarketDataResponse> processor) {
        log.info("Подписка на стрим {} для инструментов {}", streamId, figis);
        investApi
                .getMarketDataStreamService()
                .newStream(
                        streamId, processor,
                        t -> log.error("Ошибка при работе стрима {}", streamId)
                )
                .subscribeCandles(
                        new ArrayList<>(figis),
                        SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE
                );
    }
}
