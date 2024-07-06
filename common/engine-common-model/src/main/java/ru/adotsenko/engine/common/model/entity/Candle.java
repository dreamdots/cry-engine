package ru.adotsenko.engine.common.model.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Candle extends VersionedEntity {
    /**
     * Financial Instrument Global Identifier - глобальный идентификатор финансового инструмента.
     * Представляет собой 12-символьный код из латинских букв и цифр, определяется как идентификатор
     * ценной бумаги на торговой площадке (бирже), которая является некоторым "источником цен".
     */
    @NotEmpty
    @EqualsAndHashCode.Include
    private String figi;
    /**
     * Канал источник
     */
    @NotNull
    @EqualsAndHashCode.Include
    private Channel channel;
    /**
     * Самая высокая цена свечи
     */
    @NotNull
    private BigDecimal highestPrice;
    /**
     * Самая низкая цена свечи
     */
    @NotNull
    private BigDecimal lowestPrice;
    /**
     * Цена открытия свечи
     */
    @NotNull
    private BigDecimal openPrice;
    /**
     * Цена закрытия свечи
     */
    @NotNull
    private BigDecimal closingPrice;
    /**
     * Дата/Время
     */
    @NotNull
    @EqualsAndHashCode.Include
    private Instant dateTime;
    /**
     * Временной интервал
     */
    @NotNull
    @EqualsAndHashCode.Include
    private Interval interval;
}

