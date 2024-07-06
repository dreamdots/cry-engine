package ru.adotsenko.engine.common.model.entity;

import lombok.*;
import org.springframework.data.annotation.Version;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class VersionedEntity extends BaseEntity {

    @Version
    @EqualsAndHashCode.Include
    private Long version;
}
