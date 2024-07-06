package ru.adotsenko.engine.tinkoff.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.tinkoff.piapi.core.InvestApi;

@ComponentScan(basePackages = "ru.adotsenko.engine.tinkoff.client")
@Configuration
@Profile("tinkoff")
@EnableConfigurationProperties(TinkoffApiConfigurationProperties.class)
public class TinkoffApiConfiguration {

    @Bean
    public InvestApi investApi(TinkoffApiConfigurationProperties tinkoffApiConfigurationProperties) {
        if (tinkoffApiConfigurationProperties.getIsSandBoxMode()) {
            return InvestApi.createSandbox(tinkoffApiConfigurationProperties.getAuthToken());
        }

        return InvestApi.create(tinkoffApiConfigurationProperties.getAuthToken());
    }
}
