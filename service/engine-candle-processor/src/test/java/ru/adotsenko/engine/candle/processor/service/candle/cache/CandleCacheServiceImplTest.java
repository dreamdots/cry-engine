package ru.adotsenko.engine.candle.processor.service.candle.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import ru.adotsenko.engine.candle.processor.service.AbstractServiceTest;
import ru.adotsenko.engine.common.model.test.CandleTestUtil;

class CandleCacheServiceImplTest extends AbstractServiceTest {

    @Autowired
    CandleCacheService candleCacheService;

    @Autowired
    CandleCacheProperties candleCacheProperties;

    @Test
    void testCreateCache() {
        var candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleCacheService.put(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.getName().equals(CandleCacheServiceImpl.buildCacheName(candle)))
                .verifyComplete();
    }

    @Test
    void testPut() {
        var candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleCacheService.put(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.getName().equals(CandleCacheServiceImpl.buildCacheName(candle)))
                .verifyComplete();

        StepVerifier.create(candleCacheService.getLast(candle))
                .expectNext(candle)
                .verifyComplete();
    }

    @Test
    void testPutAll() {
        int size = candleCacheProperties.getMaxSize();
        var candles = CandleTestUtil.generateRandomCandles(size * 2);

        StepVerifier.create(candleCacheService.putAll(candles))
                .expectNextSequence(candles)
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.getName().equals(CandleCacheServiceImpl.buildCacheName(candles.get(0))))
                .verifyComplete();

        StepVerifier.create(candleCacheService.size(candles.get(0)))
                .expectNextMatches(i -> i == size)
                .verifyComplete();
    }

    @Test
    void testGetAll() {
        int size = candleCacheProperties.getMaxSize();
        var candles = CandleTestUtil.generateRandomCandles(size);

        StepVerifier.create(candleCacheService.putAll(candles))
                .expectNextSequence(candles)
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.getName().equals(CandleCacheServiceImpl.buildCacheName(candles.get(0))))
                .verifyComplete();

        StepVerifier.create(candleCacheService.getAll(candles.get(0), size))
                .expectNextSequence(candles)
                .verifyComplete();
    }

    @Test
    void testClear() {
        int size = 5;
        var candles = CandleTestUtil.generateRandomCandles(size);

        StepVerifier.create(candleCacheService.putAll(candles))
                .expectNextSequence(candles)
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.getName().equals(CandleCacheServiceImpl.buildCacheName(candles.get(0))))
                .verifyComplete();

        StepVerifier.create(candleCacheService.clear(candles.get(0)))
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.size() == 0)
                .verifyComplete();
    }

    @Test
    void testGetLast() {
        var candle1 = CandleTestUtil.generateRandomCandle();
        var candle2 = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(candleCacheService.putAll(candle1, candle2))
                .expectNext(candle1, candle2)
                .verifyComplete();

        StepVerifier.create(candleCacheService.caches())
                .expectNextMatches(cc -> cc.getName().equals(CandleCacheServiceImpl.buildCacheName(candle1)))
                .verifyComplete();

        StepVerifier.create(candleCacheService.getLast(candle1))
                .expectNext(candle2)
                .verifyComplete();
    }
}