package ru.adotsenko.engine.candle.processor.service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;

import java.time.Instant;

@Repository
public interface CandleRepository extends R2dbcRepository<Candle, Long> {

    Flux<Candle> findAllByFigiAndChannelAndIntervalOrderByDateTime(String figi, Channel channel, Interval interval);

    Mono<Candle> findFirstByFigiAndDateTimeAndIntervalAndChannel(String figi, Instant dateTime, Interval interval, Channel channel);

    Mono<Candle> findFirstByFigiAndChannelAndIntervalOrderByDateTime(String figi, Channel channel, Interval interval);

    Mono<Candle> findFirstByFigiAndChannelAndIntervalOrderByDateTimeDesc(String figi, Channel channel, Interval interval);
}
