package com.exchange.rate.services.impl;

import com.exchange.rate.config.ApplicationConfig;
import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.dto.response.ExternalRestResponse;
import com.exchange.rate.exceptions.ExternalRestException;
import com.exchange.rate.services.ExchangeRateService;
import com.exchange.rate.util.BuilderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.exchange.rate.util.Constants.ERROR_PARSE_MESSAGE;
import static com.exchange.rate.util.Constants.ERROR_REST_MESSAGE;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ApplicationConfig applicationConfig;

    private final WebClient webClient;

    public ExchangeRateServiceImpl(final ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.webClient = BuilderUtil.buildWebClient(applicationConfig.getApiServiceUrl());
    }

    @Override
    public Mono<ExchangeRateResponse> exchangeRate(ExchangeRateRequest exchangeRateRequest) {
        return getExchangeRate(exchangeRateRequest);
    }

    private Mono<ExchangeRateResponse> getExchangeRate(final ExchangeRateRequest exchangeRateRequest) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(
                                applicationConfig.getApiServiceParamSource(), exchangeRateRequest.getSourceCurrency())
                        .queryParam(
                                applicationConfig.getApiServiceParamTarget(), exchangeRateRequest.getTargetCurrency())
                        .build())
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, clientResponse ->
                        Mono.error(new ExternalRestException(ERROR_REST_MESSAGE)))
                .bodyToMono(ExternalRestResponse.class)
                .map(externalRestResponse -> BuilderUtil.buildExchangeRateResponse(
                        exchangeRateRequest, externalRestResponse.getRate(), externalRestResponse.getId()))
                .doOnError(throwable -> {
                    if (throwable instanceof ExternalRestException exception) {
                        throw exception;
                    }
                    throw new ExternalRestException(ERROR_PARSE_MESSAGE, throwable);
                });
    }
}
