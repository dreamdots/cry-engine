package ru.adotsenko.engine.common.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@SuppressWarnings("DefaultAnnotationParam")
@Getter
@Setter
@ToString(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
}
