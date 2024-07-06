package ru.adotsenko.engine.tinkoff.client;

import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.util.Collection;

public interface TinkoffStreamApi {
    void subscribeToCandlesStream(Collection<String> figis,
                                  String streamId,
                                  StreamProcessor<MarketDataResponse> processor);
}
