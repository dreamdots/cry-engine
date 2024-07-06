package ru.adotsenko.engine.candle.processor.service.instrument;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.adotsenko.engine.candle.processor.service.repository.InstrumentRepository;
import ru.adotsenko.engine.common.model.entity.Instrument;
import ru.adotsenko.engine.common.model.entity.InstrumentType;
import ru.adotsenko.engine.tinkoff.client.TinkoffApi;

import java.util.Collection;

@Slf4j
@Service
public class InstrumentServiceImpl implements InstrumentService {
    private final InstrumentService instrumentService;
    private final InstrumentRepository instrumentRepository;
    private final TinkoffApi tinkoffApi;
    private final InstrumentMapper instrumentMapper;

    public InstrumentServiceImpl(@Lazy InstrumentService instrumentService,
                                 InstrumentRepository instrumentRepository,
                                 @Autowired(required = false) TinkoffApi tinkoffApi,
                                 InstrumentMapper instrumentMapper) {
        this.instrumentService = instrumentService;
        this.instrumentRepository = instrumentRepository;
        this.tinkoffApi = tinkoffApi;
        this.instrumentMapper = instrumentMapper;
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public Flux<Instrument> refreshInstrumentsFromRemote() {
        if (tinkoffApi == null) {
            log.warn("Интеграция с Тинькофф выключена");
            return Flux.empty();
        }

        var p2 = tinkoffApi.getTradableBonds().map(instrumentMapper::map);
        var p4 = tinkoffApi.getTradableEtfs().map(instrumentMapper::map);
        var p1 = tinkoffApi.getTradableCurrencies().map(instrumentMapper::map);
        var p3 = tinkoffApi.getTradableShares().map(instrumentMapper::map);

        return Flux.merge(p1, p2, p3, p4)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(instrumentService::save);
    }

    @Override
    public Mono<Instrument> findByFigi(String figi) {
        return instrumentRepository.findFirstByFigi(figi);
    }

    @Override
    public Mono<Boolean> existByFigi(String figi) {
        return instrumentService.findByFigi(figi).hasElement();
    }

    @Override
    @Transactional
    public Mono<Instrument> save(Instrument instrument) {
        return instrumentRepository
                .findFirstByFigi(instrument.getFigi())
                .flatMap(i -> {
                    log.debug("Обновлен инструмент {}", i);
                    instrument.setId(i.getId());
                    return instrumentRepository.save(instrument);
                })
                .switchIfEmpty(instrumentRepository
                        .save(instrument)
                        .doOnNext(i -> log.debug("Добавлен новый инструмент {}", i))
                );
    }

    @Override
    public Flux<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    @Override
    public Flux<Instrument> findAllByType(InstrumentType type) {
        return instrumentRepository.findAllByType(type);
    }

    @Override
    @Transactional
    public Flux<Instrument> saveAll(Collection<Instrument> instruments) {
        return Flux
                .fromIterable(instruments)
                .flatMap(instrumentService::save);
    }
}
