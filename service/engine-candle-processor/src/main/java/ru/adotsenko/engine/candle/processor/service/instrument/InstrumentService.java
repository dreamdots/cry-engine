package ru.adotsenko.engine.candle.processor.service.instrument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.entity.Instrument;
import ru.adotsenko.engine.common.model.entity.InstrumentType;

import java.util.Collection;

public interface InstrumentService {

    Flux<Instrument> refreshInstrumentsFromRemote();

    Mono<Instrument> findByFigi(String figi);

    Mono<Boolean> existByFigi(String figi);

    Mono<Instrument> save(Instrument instrument);

    Flux<Instrument> findAll();

    Flux<Instrument> findAllByType(InstrumentType type);

    Flux<Instrument> saveAll(Collection<Instrument> instruments);
}
