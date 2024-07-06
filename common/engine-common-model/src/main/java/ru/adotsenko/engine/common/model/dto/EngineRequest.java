package ru.adotsenko.engine.common.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EngineRequest<T> {
    private T data;
}
