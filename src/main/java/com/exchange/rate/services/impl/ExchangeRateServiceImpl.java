package com.exchange.rate.services.impl;

import com.exchange.rate.config.ApplicationConfig;
import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.dto.response.ExternalRestResponse;
import com.exchange.rate.exceptions.ExternalRestException;
import com.exchange.rate.services.DefaultExchangeRateService;
import com.exchange.rate.services.ExchangeRateService;
import com.exchange.rate.util.BuilderUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.exchange.rate.util.Constants.CIRCUIT_BREAKER_EXCHANGE_RATE_NAME;
import static com.exchange.rate.util.Constants.ERROR_PARSE_MESSAGE;
import static com.exchange.rate.util.Constants.ERROR_REST_MESSAGE;

@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ApplicationConfig applicationConfig;

    private final DefaultExchangeRateService defaultExchangeRateService;

    private final WebClient webClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_EXCHANGE_RATE_NAME, fallbackMethod = "getDefaultExchangeRate")
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
                .onErrorMap(throwable -> {
                    if (throwable instanceof ExternalRestException exception) {
                        throw exception;
                    }
                    throw new ExternalRestException(ERROR_PARSE_MESSAGE, throwable);
                });
    }

    public Mono<ExchangeRateResponse> getDefaultExchangeRate(final ExchangeRateRequest exchangeRateRequest,
                                                              final Throwable throwable) {
        return this.defaultExchangeRateService.exchangeRate(exchangeRateRequest);
    }
}
