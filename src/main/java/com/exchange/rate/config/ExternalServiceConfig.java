package com.exchange.rate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ExternalServiceConfig {

    @Value("${api.service.url}")
    private String apiServiceUrl;

    @Bean
    WebClient webClient() {
        return WebClient.builder().baseUrl(apiServiceUrl).build();
    }
}