package ru.adotsenko.cryengine.common.featuretoggle;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class FeatureToggle {
    @NotNull(message = "Состояние фича-тоггла должно быть явно указано (isEnabled)")
    private Boolean isEnabled;
    @NotEmpty(message = "Должно быть задано описание фича-тоггла (description)")
    private String description;
    @NotEmpty(message = "Название компонента (usedIn) должно быть заполнено")
    private String usedIn;
    private String youtrackTask;
    private Object[] additionalOptions;

    public FeatureToggle() {
    }

    public FeatureToggle(Boolean isEnabled, String description, String usedIn) {
        this.isEnabled = isEnabled;
        this.description = description;
        this.usedIn = usedIn;
    }
}
