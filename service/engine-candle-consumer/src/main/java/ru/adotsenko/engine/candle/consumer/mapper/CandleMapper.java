package ru.adotsenko.engine.candle.consumer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.adotsenko.engine.common.model.entity.Candle;

@Mapper(componentModel = "spring", uses = MappingExtension.class)
public interface CandleMapper {

    @Mapping(target = "interval", constant = "_1MIN")
    @Mapping(target = "figi", source = "figi")
    @Mapping(target = "highestPrice", source = "high")
    @Mapping(target = "lowestPrice", source = "low")
    @Mapping(target = "openPrice", source = "open")
    @Mapping(target = "closingPrice", source = "close")
    @Mapping(target = "dateTime", source = "time")
    @Mapping(target = "channel", constant = "TINKOFF")
    Candle map(ru.tinkoff.piapi.contract.v1.Candle candle);

    @Mapping(target = "interval", constant = "_1MIN")
    @Mapping(target = "figi", source = "figi")
    @Mapping(target = "channel", constant = "TINKOFF")
    @Mapping(target = "highestPrice", source = "historicCandle.high")
    @Mapping(target = "lowestPrice", source = "historicCandle.low")
    @Mapping(target = "openPrice", source = "historicCandle.open")
    @Mapping(target = "closingPrice", source = "historicCandle.close")
    @Mapping(target = "dateTime", source = "historicCandle.time")
    Candle map(ru.tinkoff.piapi.contract.v1.HistoricCandle historicCandle, String figi);
}
