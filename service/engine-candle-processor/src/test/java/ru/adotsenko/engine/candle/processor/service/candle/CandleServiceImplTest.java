package ru.adotsenko.engine.candle.processor.service.candle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;
import ru.adotsenko.engine.candle.processor.service.AbstractServiceTest;
import ru.adotsenko.engine.candle.processor.service.candle.cache.CandleCache;
import ru.adotsenko.engine.candle.processor.service.candle.cache.CandleCacheProperties;
import ru.adotsenko.engine.candle.processor.service.candle.cache.CandleCacheService;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.test.CandleTestUtil;

import java.math.BigDecimal;
import java.util.List;

class CandleServiceImplTest extends AbstractServiceTest {

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    CandleCacheProperties candleCacheProperties;

    @Autowired
    CandleService candleService;

    @Autowired
    CandleCacheService candleCacheService;

    @AfterEach
    @BeforeEach
    void cleanup() {
        r2dbcEntityTemplate
                .delete(Candle.class)
                .all()
                .blockOptional();

        candleCacheService
                .caches()
                .flatMap(CandleCache::clear)
                .blockLast();
    }

    @Test
    void testSave() {
        var candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleService.save(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(candleService.find(candle.getFigi(), candle.getChannel(), candle.getInterval(), candle.getDateTime()))
                .expectNext(candle)
                .verifyComplete();
    }

    @Test
    void testSaveAll() {
        var batch1 = CandleTestUtil.generateRandomCandles(candleCacheProperties.getMaxSize());
        var batch2 = CandleTestUtil.generateRandomCandles(candleCacheProperties.getMaxSize());

        StepVerifier.create(candleService.saveAll(batch1))
                .expectNextCount(batch1.size())
                .verifyComplete();

        StepVerifier.create(candleService.saveAll(batch2))
                .expectNextCount(batch2.size())
                .verifyComplete();

        var firstCandle = batch1.get(0);
        StepVerifier.create(candleService.findAll(firstCandle.getFigi(), firstCandle.getChannel(), firstCandle.getInterval(), 5000))
                .expectNextSequence(batch2)
                .verifyComplete();
    }

    @Test
    void testVersionedSaveAll() {
        var candle1 = CandleTestUtil.generateRandomCandle();
        var candle2 = CandleTestUtil.copy(candle1);
        var candle3 = CandleTestUtil.copy(candle2);
        var candle4 = CandleTestUtil.copy(candle3);

        var batch = List.of(candle1, candle2, candle3, candle4);

        StepVerifier.create(candleService.saveAll(batch))
                .expectNextCount(batch.size())
                .verifyComplete();

        StepVerifier.create(candleService.findLast(candle4.getFigi(), candle4.getChannel(), candle4.getInterval()))
                .expectNext(candle4)
                .verifyComplete();
    }

    @Test
    void testUpdate() {
        var candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleService.save(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(candleService.find(candle.getFigi(), candle.getChannel(), candle.getInterval(), candle.getDateTime()))
                .expectNext(candle)
                .verifyComplete();

        candle.setClosingPrice(BigDecimal.ONE);
        candle.setOpenPrice(BigDecimal.ONE);
        candle.setHighestPrice(BigDecimal.ONE);
        candle.setLowestPrice(BigDecimal.ONE);

        StepVerifier.create(candleService.save(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(candleService.find(candle.getFigi(), candle.getChannel(), candle.getInterval(), candle.getDateTime()))
                .expectNext(candle)
                .verifyComplete();
    }

    @Test
    void testFindFirst() {
        var candle1 = CandleTestUtil.generateRandomCandle();
        var candle2 = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleService.save(candle1))
                .expectNext(candle1)
                .verifyComplete();

        StepVerifier.create(candleService.save(candle2))
                .expectNext(candle2)
                .verifyComplete();

        StepVerifier.create(candleService.findFirst(candle1.getFigi(), candle1.getChannel(), candle1.getInterval()))
                .expectNext(candle1)
                .verifyComplete();
    }

    @Test
    void testFindLast() {
        var candle1 = CandleTestUtil.generateRandomCandle();
        var candle2 = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleService.save(candle1))
                .expectNext(candle1)
                .verifyComplete();

        StepVerifier.create(candleService.save(candle2))
                .expectNext(candle2)
                .verifyComplete();

        StepVerifier.create(candleService.findLast(candle2.getFigi(), candle2.getChannel(), candle2.getInterval()))
                .expectNext(candle2)
                .verifyComplete();
    }
}