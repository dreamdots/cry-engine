package ru.adotsenko.cryengine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ping/")
public class PingController {
    @GetMapping
    public Mono<String> ping() {
        return Mono.just("Pong");
    }
}
