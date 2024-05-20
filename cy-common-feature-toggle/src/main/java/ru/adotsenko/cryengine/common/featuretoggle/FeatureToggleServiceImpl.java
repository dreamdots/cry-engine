package ru.adotsenko.cryengine.common.featuretoggle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeatureToggleServiceImpl implements FeatureToggleService {
    private final FeatureToggleConfigurationProperties properties;

    @Override
    public boolean isEnabled(String featureName) {
        return Optional
                .ofNullable(getFeatureToggle(featureName))
                .map(FeatureToggle::getIsEnabled)
                .orElse(Boolean.FALSE);
    }

    @Override
    public Collection<FeatureToggle> getAll() {
        return properties.getFeatures().values();
    }

    @Override
    public FeatureToggle getFeatureToggle(String featureName) {
        return Optional
                .ofNullable(featureName)
                .flatMap(fn -> {
                    var opt = Optional.ofNullable(properties.getFeatures().get(fn));
                    if (opt.isPresent()) {
                        log.debug("Found feature {} with state {}", featureName, opt.get().getIsEnabled());
                    } else {
                        log.debug("Feature {} doesn't exists", featureName);
                    }

                    return opt;
                })
                .orElse(null);
    }

    @Override
    public boolean enableFeatureToggle(String featureName) {
        return Optional
                .ofNullable(getFeatureToggle(featureName))
                .map(ft -> {
                    log.debug("Enable feature {}", featureName);
                    ft.setIsEnabled(Boolean.TRUE);
                    return Boolean.TRUE;
                })
                .orElse(Boolean.FALSE);
    }

    @Override
    public boolean disableFeatureToggle(String featureName) {
        return Optional
                .ofNullable(getFeatureToggle(featureName))
                .map(ft -> {
                    log.debug("Disable feature {}", featureName);
                    ft.setIsEnabled(Boolean.FALSE);
                    return Boolean.TRUE;
                })
                .orElse(Boolean.FALSE);
    }
}
