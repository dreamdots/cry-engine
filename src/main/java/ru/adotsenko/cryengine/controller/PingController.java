package ru.adotsenko.cryengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.adotsenko.cryengine.FeatureToggleConstants;
import ru.adotsenko.cryengine.common.featuretoggle.FeatureToggleService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ping/")
public class PingController {
    private final FeatureToggleService featureToggleService;
    @GetMapping
    public Mono<String> ping() {
        if (featureToggleService.isEnabled(FeatureToggleConstants.TEST_FEATURE)) {
            return Mono.just("Toggle pong");
        }

        return Mono.just("Pong");
    }
}
