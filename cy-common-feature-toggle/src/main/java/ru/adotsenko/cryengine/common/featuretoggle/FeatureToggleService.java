package ru.adotsenko.cryengine.common.featuretoggle;

import java.util.Collection;

public interface FeatureToggleService {

    boolean isEnabled(String featureName);

    Collection<FeatureToggle> getAll();

    FeatureToggle getFeatureToggle(String featureName);

    boolean enableFeatureToggle(String featureName);

    boolean disableFeatureToggle(String featureName);
}
