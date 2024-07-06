package ru.adotsenko.engine.candle.processor.service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Instrument;
import ru.adotsenko.engine.common.model.entity.InstrumentType;

@Repository
public interface InstrumentRepository extends R2dbcRepository<Instrument, Long> {

    Mono<Instrument> findFirstByFigi(String figi);

    Flux<Instrument> findAllByType(InstrumentType type);
}
