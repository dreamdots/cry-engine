package ru.adotsenko.engine.common.featuretoggle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@ConfigurationProperties
@Getter
@Profile("feature-toggle")
@Setter
@SuppressWarnings({"ConfigurationProperties"})
public class FeatureToggleConfigurationProperties {
    private Map<@NotBlank(message = "Наименование фичи должно быть заполнено")
    @Pattern(regexp = "^[^-]*$",
            message = "Наименование фичи не должно содержать \"-\"")
            String, FeatureToggle> features = Map.of();
}
