package ru.adotsenko.cryengine.common.featuretoggle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeatureToggleServiceTest {
    @Test
    void shouldEnableFeatureToggle() {
        var properties = new FeatureToggleConfigurationProperties();
        properties.setFeatures(Map.of(
                "test-1", new FeatureToggle(true, "test", "test"),
                "test-2", new FeatureToggle(true, "test", "test")
        ));

        var service = new FeatureToggleServiceImpl(properties);

        assertTrue(service.enableFeatureToggle("test-1"));
        assertTrue(service.isEnabled("test-1"));
        assertTrue(service.enableFeatureToggle("test-2"));
        assertTrue(service.isEnabled("test-2"));

        assertFalse(service.enableFeatureToggle("test-6"));
        assertFalse(service.isEnabled("test-6"));
        assertFalse(service.enableFeatureToggle("test-7"));
        assertFalse(service.isEnabled("test-7"));
    }

    @Test
    void shouldDisableFeatureToggle() {
        var properties = new FeatureToggleConfigurationProperties();
        properties.setFeatures(Map.of(
                "test-1", new FeatureToggle(true, "test", "test"),
                "test-2", new FeatureToggle(true, "test", "test")
        ));

        var service = new FeatureToggleServiceImpl(properties);

        assertTrue(service.disableFeatureToggle("test-1"));
        assertFalse(service.isEnabled("test-1"));
        assertTrue(service.disableFeatureToggle("test-2"));
        assertFalse(service.isEnabled("test-2"));

        assertFalse(service.disableFeatureToggle("test-6"));
        assertFalse(service.isEnabled("test-6"));
        assertFalse(service.disableFeatureToggle("test-7"));
        assertFalse(service.isEnabled("test-7"));
    }

    @Test
    void shouldReturnAll() {
        var properties = new FeatureToggleConfigurationProperties();
        properties.setFeatures(Map.of(
                "test-1", new FeatureToggle(true, "test", "test"),
                "test-2", new FeatureToggle(true, "test", "test")
        ));

        var service = new FeatureToggleServiceImpl(properties);

        Assertions.assertEquals(service.getAll(), properties.getFeatures().values());
    }

    @Test
    void shouldReturnSingle() {
        var properties = new FeatureToggleConfigurationProperties();
        properties.setFeatures(Map.of(
                "test-1", new FeatureToggle(true, "test", "test"),
                "test-2", new FeatureToggle(true, "test", "test")
        ));

        var service = new FeatureToggleServiceImpl(properties);

        Assertions.assertEquals(
                service.getFeatureToggle("test-1"),
                properties.getFeatures().get("test-1")
        );
    }
}