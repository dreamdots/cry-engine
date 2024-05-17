package ru.adotsenko.cryengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = {
        PingController.class
})
class BaseControllerTest {

    @Autowired
    protected WebTestClient testClient;
}