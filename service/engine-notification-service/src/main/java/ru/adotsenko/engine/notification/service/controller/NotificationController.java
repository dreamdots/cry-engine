package ru.adotsenko.engine.notification.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.dto.EngineRequest;
import ru.adotsenko.engine.common.model.dto.SendNotificationRequest;
import ru.adotsenko.engine.notification.service.api.NotificationApi;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    @Override
    @RequestMapping(method = RequestMethod.POST, path = API_V1_NOTIFICATION_SERVICE_SEND, consumes = "application/json")
    public Mono<Void> send(@RequestBody EngineRequest<SendNotificationRequest> request) {
        log.info("Отправка сообщения {} в {}", request.getData().getMessage(), request.getData().getDestination());
        return Mono.empty();
    }
}
