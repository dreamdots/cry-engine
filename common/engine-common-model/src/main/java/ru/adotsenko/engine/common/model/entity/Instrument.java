package ru.adotsenko.engine.common.model.entity;

import lombok.*;

@Builder
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Instrument extends BaseEntity {
    /**
     * Financial Instrument Global Identifier - глобальный идентификатор финансового инструмента.
     * Представляет собой 12-символьный код из латинских букв и цифр, определяется как идентификатор
     * ценной бумаги на торговой площадке (бирже), которая является некоторым "источником цен".
     */
    @EqualsAndHashCode.Include
    private String figi;
    /**
     * Тип инструмента
     */
    private InstrumentType type;
    /**
     * Уникальный идентификатор инструмента
     */
    private String uid;
    /**
     * Название инструмента
     */
    private String name;
    /**
     * Лотность инструмента. Возможно совершение операций только на количества ценной бумаги, кратные параметру lot
     */
    private Integer lot;
    /**
     * Тикер инструмента
     */
    private String ticker;
    /**
     * Флаг, отображающий доступность торговли инструментом по выходным
     */
    private Boolean availInWeekend;
    /**
     * Валюта расчётов
     */
    private String currency;
    /**
     * Признак доступности для ИИС
     */
    private boolean availForIis;
    /**
     * Флаг, отображающий доступность торговли инструментом только для квалифицированных инвесторов
     */
    private Boolean onlyForQual;
}
