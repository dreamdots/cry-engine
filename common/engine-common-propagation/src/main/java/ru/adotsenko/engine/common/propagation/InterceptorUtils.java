package ru.adotsenko.engine.common.propagation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;

@Slf4j
@UtilityClass
public class InterceptorUtils {

    public static void setIfNotExists(HttpHeaders headers,
                                      String headerName,
                                      String headerValue) {
        headers.compute(headerName, (n, values) -> {
            if (values == null) {
                values = new ArrayList<>();

                log.debug("Set header {} value {}", headerName, headerValue);
                values.add(headerValue);
            }

            return values;
        });
    }
}
