package ru.adotsenko.engine.common.model.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Event<T> {
    private long timestamp;
    private T data;
    private UUID processId;

    public Event() {
    }

    public Event(T data) {
        this(System.currentTimeMillis(), data);
    }

    public Event(long timestamp, T data) {
        this(timestamp, data, UUID.randomUUID());
    }

    public Event(long timestamp, T data, UUID processId) {
        this.timestamp = timestamp;
        this.data = data;
        this.processId = processId;
    }

    public static <T> Event<T> of(T data) {
        return new Event<>(data);
    }

    public static <T> Event<T> of(long timestamp, T data) {
        return new Event<>(timestamp, data);
    }

    public static <T> Event<T> of(long timestamp, T data, UUID processId) {
        return new Event<>(timestamp, data, processId);
    }
}
