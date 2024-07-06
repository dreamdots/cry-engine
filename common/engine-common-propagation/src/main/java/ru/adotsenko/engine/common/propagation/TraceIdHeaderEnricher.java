package ru.adotsenko.engine.common.propagation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@SuppressWarnings("NullableProblems")
public class TraceIdHeaderEnricher implements WebFilter, Ordered {
    @Value("${propagation.filters.trace-id.order}")
    private int order;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var traceId = new AtomicReference<>(UUID.randomUUID().toString().toUpperCase());

        exchange.getRequest()
                .mutate()
                .headers(headers -> {
                    headers.compute(CustomHeader.TRACE_ID_HEADER, (headerName, value) -> {
                        if (value == null) {
                            value = new ArrayList<>();
                            value.add(traceId.get());
                            log.trace("Set {} request header to {}", CustomHeader.TRACE_ID_HEADER, traceId);
                        } else {
                            traceId.set(value.get(0));
                        }

                        return value;
                    });
                });

        exchange.getResponse()
                .getHeaders()
                .compute(CustomHeader.TRACE_ID_HEADER, (headerName, value) -> {
                    if (value == null) {
                        value = new ArrayList<>();
                        value.add(traceId.get());
                        log.trace("Set {} response header to {}", CustomHeader.TRACE_ID_HEADER, traceId);
                    }

                    return value;
                });

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return order;
    }
}
