package ru.adotsenko.engine.common.openfeign;

import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.propagation.CustomHeader;

import java.util.Collections;
import java.util.UUID;

public class FeignTradeIdHeaderEnricher implements ReactiveHttpRequestInterceptor {

    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest reactiveHttpRequest) {
        return Mono.just(reactiveHttpRequest)
                .doOnNext(req -> {
                    var traceId = UUID.randomUUID().toString().toUpperCase();
                    req.headers().putIfAbsent(CustomHeader.TRACE_ID_HEADER, Collections.singletonList(traceId));
                });
    }
}
