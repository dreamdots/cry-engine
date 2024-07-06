package ru.adotsenko.engine.common.propagation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@SuppressWarnings("NullableProblems")
public class LoggingInterceptor implements WebFilter, Ordered {
    @Value("${propagation.filters.logging.order}")
    private int order;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return order;
    }
}
