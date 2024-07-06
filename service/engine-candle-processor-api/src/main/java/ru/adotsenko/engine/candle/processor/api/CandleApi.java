package ru.adotsenko.engine.candle.processor.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.adotsenko.engine.common.model.dto.EngineResponse;
import ru.adotsenko.engine.common.model.entity.Candle;
import ru.adotsenko.engine.common.model.entity.Channel;
import ru.adotsenko.engine.common.model.entity.Interval;

import java.util.List;

@ReactiveFeignClient(name = "engine-notification-service")
public interface CandleApi {
    String API_V_1_CANDLE_PROCESSOR = "/api/v1/candle/processor";
    String API_V_1_CANDLE_PROCESSOR_FIND_ALL = API_V_1_CANDLE_PROCESSOR + "/find/all/";

    @RequestMapping(method = RequestMethod.GET, path = API_V_1_CANDLE_PROCESSOR_FIND_ALL)
    Mono<EngineResponse<List<Candle>>> findAll(@RequestParam String figi,
                                               @RequestParam(required = false, defaultValue = "_1MIN") Interval interval,
                                               @RequestParam(required = false, defaultValue = "100") Integer depth,
                                               @RequestParam(required = false, defaultValue = "TINKOFF") Channel channel);
}
