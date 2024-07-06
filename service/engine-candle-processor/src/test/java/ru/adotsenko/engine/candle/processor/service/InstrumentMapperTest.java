package ru.adotsenko.engine.candle.processor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.adotsenko.engine.candle.processor.service.instrument.InstrumentMapper;
import ru.adotsenko.engine.common.model.entity.InstrumentType;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.contract.v1.Currency;
import ru.tinkoff.piapi.contract.v1.Etf;
import ru.tinkoff.piapi.contract.v1.Share;

class InstrumentMapperTest extends AbstractServiceTest {

    @Autowired
    InstrumentMapper instrumentMapper;

    @Test
    void testMapCurrencyToInstrument() {
        var currency = Currency
                .newBuilder()
                .setCurrency("rub")
                .setUid("606864c7-845d-4783-b1e1-06f354318948")
                .setName("Узбекский сум")
                .setLot(1000000)
                .setWeekendFlag(false)
                .setCurrency("rub")
                .setTicker("UZSRUB_TOM")
                .setForIisFlag(true)
                .setForQualInvestorFlag(false)
                .build();

        var instrument = instrumentMapper.map(currency);

        Assertions.assertNotNull(instrument);
        Assertions.assertEquals("606864c7-845d-4783-b1e1-06f354318948", instrument.getUid());
        Assertions.assertEquals("Узбекский сум", instrument.getName());
        Assertions.assertEquals(1000000, instrument.getLot());
        Assertions.assertFalse(instrument.getAvailInWeekend());
        Assertions.assertEquals("rub", instrument.getCurrency());
        Assertions.assertEquals("UZSRUB_TOM", instrument.getTicker());
        Assertions.assertTrue(instrument.isAvailForIis());
        Assertions.assertFalse(instrument.getOnlyForQual());
        Assertions.assertEquals(instrument.getType(), InstrumentType.CURRENCY);
    }

    @Test
    void testMapBondToInstrument() {
        var currency = Bond
                .newBuilder()
                .setCurrency("rub")
                .setUid("606864c7-845d-4783-b1e1-06f354318948")
                .setName("Узбекский сум")
                .setLot(1000000)
                .setWeekendFlag(false)
                .setCurrency("rub")
                .setTicker("UZSRUB_TOM")
                .setForIisFlag(true)
                .setForQualInvestorFlag(false)
                .build();

        var instrument = instrumentMapper.map(currency);

        Assertions.assertNotNull(instrument);
        Assertions.assertEquals("606864c7-845d-4783-b1e1-06f354318948", instrument.getUid());
        Assertions.assertEquals("Узбекский сум", instrument.getName());
        Assertions.assertEquals(1000000, instrument.getLot());
        Assertions.assertFalse(instrument.getAvailInWeekend());
        Assertions.assertEquals("rub", instrument.getCurrency());
        Assertions.assertEquals("UZSRUB_TOM", instrument.getTicker());
        Assertions.assertTrue(instrument.isAvailForIis());
        Assertions.assertFalse(instrument.getOnlyForQual());
        Assertions.assertEquals(instrument.getType(), InstrumentType.BOND);
    }

    @Test
    void testMapShareToInstrument() {
        var currency = Share
                .newBuilder()
                .setCurrency("rub")
                .setUid("606864c7-845d-4783-b1e1-06f354318948")
                .setName("Узбекский сум")
                .setLot(1000000)
                .setWeekendFlag(false)
                .setCurrency("rub")
                .setTicker("UZSRUB_TOM")
                .setForIisFlag(true)
                .setForQualInvestorFlag(false)
                .build();

        var instrument = instrumentMapper.map(currency);

        Assertions.assertNotNull(instrument);
        Assertions.assertEquals("606864c7-845d-4783-b1e1-06f354318948", instrument.getUid());
        Assertions.assertEquals("Узбекский сум", instrument.getName());
        Assertions.assertEquals(1000000, instrument.getLot());
        Assertions.assertFalse(instrument.getAvailInWeekend());
        Assertions.assertEquals("rub", instrument.getCurrency());
        Assertions.assertEquals("UZSRUB_TOM", instrument.getTicker());
        Assertions.assertTrue(instrument.isAvailForIis());
        Assertions.assertFalse(instrument.getOnlyForQual());
        Assertions.assertEquals(instrument.getType(), InstrumentType.SHARE);
    }

    @Test
    void testMapEtfToInstrument() {
        var currency = Etf
                .newBuilder()
                .setCurrency("rub")
                .setUid("606864c7-845d-4783-b1e1-06f354318948")
                .setName("Узбекский сум")
                .setLot(1000000)
                .setWeekendFlag(false)
                .setCurrency("rub")
                .setTicker("UZSRUB_TOM")
                .setForIisFlag(true)
                .setForQualInvestorFlag(false)
                .build();

        var instrument = instrumentMapper.map(currency);

        Assertions.assertNotNull(instrument);
        Assertions.assertEquals("606864c7-845d-4783-b1e1-06f354318948", instrument.getUid());
        Assertions.assertEquals("Узбекский сум", instrument.getName());
        Assertions.assertEquals(1000000, instrument.getLot());
        Assertions.assertFalse(instrument.getAvailInWeekend());
        Assertions.assertEquals("rub", instrument.getCurrency());
        Assertions.assertEquals("UZSRUB_TOM", instrument.getTicker());
        Assertions.assertTrue(instrument.isAvailForIis());
        Assertions.assertFalse(instrument.getOnlyForQual());
        Assertions.assertEquals(instrument.getType(), InstrumentType.ETF);
    }
}