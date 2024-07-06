package ru.adotsenko.engine.candle.processor.service.instrument;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.adotsenko.engine.common.model.entity.Instrument;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.contract.v1.Currency;
import ru.tinkoff.piapi.contract.v1.Etf;
import ru.tinkoff.piapi.contract.v1.Share;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    @Mapping(target = "figi", source = "figi")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lot", source = "lot")
    @Mapping(target = "type", constant = "CURRENCY")
    @Mapping(target = "ticker", source = "ticker")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "availInWeekend", source = "weekendFlag")
    @Mapping(target = "availForIis", source = "forIisFlag")
    @Mapping(target = "onlyForQual", source = "forQualInvestorFlag")
    Instrument map(Currency currency);

    @Mapping(target = "figi", source = "figi")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lot", source = "lot")
    @Mapping(target = "type", constant = "BOND")
    @Mapping(target = "ticker", source = "ticker")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "availInWeekend", source = "weekendFlag")
    @Mapping(target = "availForIis", source = "forIisFlag")
    @Mapping(target = "onlyForQual", source = "forQualInvestorFlag")
    Instrument map(Bond bond);

    @Mapping(target = "figi", source = "figi")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lot", source = "lot")
    @Mapping(target = "type", constant = "SHARE")
    @Mapping(target = "ticker", source = "ticker")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "availInWeekend", source = "weekendFlag")
    @Mapping(target = "availForIis", source = "forIisFlag")
    @Mapping(target = "onlyForQual", source = "forQualInvestorFlag")
    Instrument map(Share share);

    @Mapping(target = "figi", source = "figi")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lot", source = "lot")
    @Mapping(target = "type", constant = "ETF")
    @Mapping(target = "ticker", source = "ticker")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "availInWeekend", source = "weekendFlag")
    @Mapping(target = "availForIis", source = "forIisFlag")
    @Mapping(target = "onlyForQual", source = "forQualInvestorFlag")
    Instrument map(Etf etf);
}
