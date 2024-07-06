package ru.adotsenko.engine.common.model.test;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class CandleTestUtil {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private static final AtomicLong TM_GENERATOR = new AtomicLong(System.currentTimeMillis());

    public static List<Candle> generateRandomCandles(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> generateRandomCandle())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static Candle generateRandomCandle() {
        var candle = new Candle();

        var start = randomDecimal();
        candle.setLowestPrice(start.subtract((start.divide(BigDecimal.valueOf(100)))));
        candle.setHighestPrice(start.add((start.divide(BigDecimal.valueOf(100)))));
        candle.setOpenPrice(candle.getLowestPrice());
        candle.setClosingPrice(candle.getHighestPrice());
        candle.setFigi("TEST");
        candle.setInterval(Interval._1MIN);
        candle.setDateTime(Instant.ofEpochMilli(TM_GENERATOR.getAndIncrement()));
        candle.setChannel(Channel.TINKOFF);
        candle.setId(ID_GENERATOR.getAndIncrement());

        return candle;
    }

    @SneakyThrows
    public static Candle copy(Candle target) {
        var candle = new Candle();

        candle.setLowestPrice(target.getLowestPrice());
        candle.setHighestPrice(target.getHighestPrice());
        candle.setOpenPrice(target.getOpenPrice());
        candle.setClosingPrice(target.getHighestPrice());
        candle.setFigi(target.getFigi());
        candle.setInterval(target.getInterval());
        candle.setId(target.getId());
        candle.setDateTime(target.getDateTime());
        candle.setChannel(target.getChannel());

        return candle;
    }

    private static BigDecimal randomDecimal() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 1_000_000));
    }
}
