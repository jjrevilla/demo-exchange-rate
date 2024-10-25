package com.exchange.rate.services;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

    Mono<ExchangeRateResponse> exchangeRate(final ExchangeRateRequest exchangeRateRequest);
}