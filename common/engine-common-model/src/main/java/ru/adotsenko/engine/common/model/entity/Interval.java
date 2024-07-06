package ru.adotsenko.engine.common.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Interval {
    _1MIN("1min"),
    _2MIN("2min"),
    _3MIN("3min"),
    _5MIN("5min"),
    _10MIN("10min"),
    _15MIN("15min"),
    _30MIN("30min"),
    HOUR("hour"),
    _2HOUR("2hour"),
    _4HOUR("4hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private final String description;
}
