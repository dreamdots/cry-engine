package ru.adotsenko.cryengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.adotsenko.cryengine.common.featuretoggle.FeatureToggleService;

@WebFluxTest(controllers = {
        PingController.class
})
@ComponentScan(basePackages = {
        "ru.adotsenko.cryengine"
})
class BaseControllerTest {
    @Autowired
    protected FeatureToggleService featureToggleService;
    @Autowired
    protected WebTestClient testClient;
}