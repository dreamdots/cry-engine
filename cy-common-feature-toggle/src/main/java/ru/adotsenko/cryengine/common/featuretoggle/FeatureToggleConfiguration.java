package ru.adotsenko.cryengine.common.featuretoggle;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@ComponentScan("ru.adotsenko.cryengine.common.featuretoggle")
@Configuration
@Profile("feature-toggles")
@EnableConfigurationProperties(FeatureToggleConfigurationProperties.class)
public class FeatureToggleConfiguration { }
