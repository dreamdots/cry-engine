package ru.adotsenko.engine.candle.processor.service.candle.cache;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.test.CandleTestUtil;

import java.util.Set;

class InMemoryCandleCacheTest {

    @Test
    void testPut() {
        InMemoryCandleCache cache = new InMemoryCandleCache("TEST", 10);
        Candle candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(cache.put(candle))
                .expectNext(candle)
                .verifyComplete();
    }

    @Test
    void testGetLast() {
        InMemoryCandleCache cache = new InMemoryCandleCache("TEST", 10);
        Candle candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(cache.put(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(cache.getLast())
                .expectNext(candle)
                .verifyComplete();
    }

    @Test
    void testGet() {
        InMemoryCandleCache cache = new InMemoryCandleCache("TEST", 10);
        Candle candle = CandleTestUtil.generateRandomCandle();

        StepVerifier.create(cache.put(candle))
                .expectNext(candle)
                .verifyComplete();

        StepVerifier.create(cache.get(candle.getDateTime()))
                .expectNext(candle)
                .verifyComplete();
    }

    @Test
    void testPutAll() {
        InMemoryCandleCache cache = new InMemoryCandleCache("TEST", 10);
        Candle candle1 = CandleTestUtil.generateRandomCandle();
        Candle candle2 = CandleTestUtil.generateRandomCandle();
        Candle candle3 = CandleTestUtil.generateRandomCandle();
        Set<Candle> candles = Set.of(candle1, candle2, candle3);

        StepVerifier.create(cache.putAll(candles))
                .expectNextCount(3)
                .verifyComplete();

        StepVerifier.create(cache.getAll(candles.size()))
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testGetAllOrdering() {
        int maxSize = 2;
        InMemoryCandleCache cache = new InMemoryCandleCache("TEST", maxSize);
        Candle candle1 = CandleTestUtil.generateRandomCandle();
        Candle candle2 = CandleTestUtil.generateRandomCandle();
        Candle candle3 = CandleTestUtil.generateRandomCandle();
        Set<Candle> candles = Set.of(candle1, candle2, candle3);

        StepVerifier.create(cache.putAll(candle1, candle3, candle2))
                .expectNextCount(candles.size())
                .verifyComplete();

        StepVerifier.create(cache.getAll(maxSize))
                .expectNext(candle2, candle3)
                .verifyComplete();

        StepVerifier.create(cache.clear())
                .verifyComplete();

        StepVerifier.create(cache.putAll(candle2, candle3, candle1))
                .expectNextCount(candles.size())
                .verifyComplete();

        StepVerifier.create(cache.getAll(maxSize))
                .expectNext(candle2, candle3)
                .verifyComplete();
    }

    @Test
    void testGetAllCounting() {
        int maxSize = 10;
        InMemoryCandleCache cache = new InMemoryCandleCache("TEST", maxSize);
        Candle candle1 = CandleTestUtil.generateRandomCandle();
        Candle candle2 = CandleTestUtil.generateRandomCandle();
        Candle candle3 = CandleTestUtil.generateRandomCandle();
        Set<Candle> candles = Set.of(candle1, candle2, candle3);

        StepVerifier.create(cache.putAll(candles))
                .expectNextCount(3)
                .verifyComplete();

        StepVerifier.create(cache.getAll(2))
                .expectNext(candle2, candle3)
                .verifyComplete();
    }
}