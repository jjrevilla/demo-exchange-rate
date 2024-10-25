package com.exchange.rate.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ApplicationConfig {

    @Value("${api.service.url}")
    private String apiServiceUrl;

    @Value("${api.service.param.source}")
    private String apiServiceParamSource;

    @Value("${api.service.param.target}")
    private String apiServiceParamTarget;
}