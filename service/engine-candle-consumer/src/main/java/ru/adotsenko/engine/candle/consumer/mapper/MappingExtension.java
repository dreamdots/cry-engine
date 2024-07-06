package ru.adotsenko.engine.candle.consumer.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class MappingExtension {

    public BigDecimal map(Quotation quotation) {
        return new BigDecimal(quotation.getUnits() + "." + quotation.getNano());
    }

    public Instant map(Timestamp ts) {
        return Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos());
    }
}
