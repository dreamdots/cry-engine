package ru.adotsenko.engine.candle.processor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.adotsenko.engine.candle.processor.service.AbstractServiceTest;
import ru.adotsenko.engine.candle.processor.service.candle.CandleService;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.test.CandleTestUtil;

import java.util.Map;

class CandleControllerTest extends AbstractServiceTest {

    @Autowired
    CandleService candleService;

    @Autowired
    CandleController candleController;

    @LocalServerPort
    Integer port;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient
                .bindToController(candleController)
                .configureClient()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    //    @Test
    void testFindAll() {
        var candles = CandleTestUtil.generateRandomCandles(10);
        var firstCandle = candles.get(0);
        webTestClient.get()
                .uri(CandleController.API_V_1_CANDLE_PROCESSOR_FIND_ALL,
                        Map.of("figi", firstCandle.getFigi()))
//                .attribute("figi", firstCandle.getFigi())
//                .attribute("interval", firstCandle.getInterval())
//                .attribute("depth", candles.size())
//                .attribute("channel", firstCandle.getChannel())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Candle.class).contains(candles.toArray(new Candle[]{}));
    }
}