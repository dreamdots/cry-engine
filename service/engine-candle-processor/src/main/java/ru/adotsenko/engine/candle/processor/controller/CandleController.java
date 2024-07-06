package ru.adotsenko.engine.candle.processor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.candle.processor.api.CandleApi;
import ru.adotsenko.engine.candle.processor.service.candle.CandleService;
import ru.adotsenko.engine.common.model.dto.EngineRequest;
import ru.adotsenko.engine.common.model.dto.EngineResponse;
import ru.adotsenko.engine.common.model.dto.NotificationDestinationType;
import ru.adotsenko.engine.common.model.dto.SendNotificationRequest;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;
import ru.adotsenko.engine.notification.service.api.NotificationApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CandleController implements CandleApi {
    private final CandleService candleService;
    private final NotificationApi notificationApi;

    @Override
    @RequestMapping(method = RequestMethod.GET, path = API_V_1_CANDLE_PROCESSOR_FIND_ALL)
    public Mono<EngineResponse<List<Candle>>> findAll(@RequestParam String figi,
                                                      @RequestParam(required = false, defaultValue = "_1MIN") Interval interval,
                                                      @RequestParam(required = false, defaultValue = "100") Integer depth,
                                                      @RequestParam(required = false, defaultValue = "TINKOFF") Channel channel) {
        return notificationApi
                .send(new EngineRequest<>(new SendNotificationRequest("Тест", NotificationDestinationType.EMAIL, "rdvaddva322@gmail.com")))
                .thenMany(candleService.findAll(figi, channel, interval, depth))
                .collectList()
                .map(EngineResponse::new);
    }
}
