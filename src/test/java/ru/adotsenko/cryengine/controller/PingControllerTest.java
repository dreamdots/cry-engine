package ru.adotsenko.cryengine.controller;

import org.junit.jupiter.api.Test;

import java.net.URI;

public class PingControllerTest extends BaseControllerTest {

    @Test
    void shouldReturnPong() {
        testClient.get()
                .uri(URI.create("/api/v1/ping/"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .isEqualTo("Pong");
    }
}
