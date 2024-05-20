package ru.adotsenko.cryengine.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.IfProfileValue;
import ru.adotsenko.cryengine.FeatureToggleConstants;

import java.net.URI;

public class PingControllerTest extends BaseControllerTest {

    @Test
    void shouldReturnPong() {
        featureToggleService.disableFeatureToggle(FeatureToggleConstants.TEST_FEATURE);
        testClient.get()
                .uri(URI.create("/api/v1/ping/"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .isEqualTo("Pong");
    }

    @Test
    void shouldReturnTogglePong() {
        featureToggleService.enableFeatureToggle(FeatureToggleConstants.TEST_FEATURE);
        testClient.get()
                .uri(URI.create("/api/v1/ping/"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .isEqualTo("Toggle pong");
    }
}
