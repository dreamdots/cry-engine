package ru.adotsenko.engine.candle.processor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.adotsenko.engine.candle.processor.service.instrument.InstrumentService;
import ru.adotsenko.engine.common.model.entity.Instrument;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/processing/instrument/")
public class InstrumentController {
    private final InstrumentService instrumentService;

    @GetMapping("/refresh/")
    public Flux<Instrument> refreshInstrumentsFromRemote() {
        return instrumentService.refreshInstrumentsFromRemote();
    }
}
