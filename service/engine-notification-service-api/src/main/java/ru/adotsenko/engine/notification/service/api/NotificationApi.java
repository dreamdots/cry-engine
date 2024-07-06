package ru.adotsenko.engine.notification.service.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.dto.EngineRequest;
import ru.adotsenko.engine.common.model.dto.SendNotificationRequest;

@ReactiveFeignClient(name = "engine-notification-service")
public interface NotificationApi {
    String API_V1_NOTIFICATION_SERVICE = "/api/v1/notification";
    String API_V1_NOTIFICATION_SERVICE_SEND = API_V1_NOTIFICATION_SERVICE + "/send";

    @RequestMapping(method = RequestMethod.POST, path = API_V1_NOTIFICATION_SERVICE_SEND, consumes = "application/json")
    Mono<Void> send(EngineRequest<SendNotificationRequest> request);
}


