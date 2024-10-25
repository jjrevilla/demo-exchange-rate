package com.exchange.rate.services.impl;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.exceptions.ResourceNotFoundException;
import com.exchange.rate.repository.ExchangeRateRepository;
import com.exchange.rate.services.DefaultExchangeRateService;
import com.exchange.rate.util.BuilderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.exchange.rate.util.Constants.CURRENCY_NOT_FOUND_MESSAGE;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultExchangeRateServiceImpl implements DefaultExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRateResponse> exchangeRate(final ExchangeRateRequest exchangeRateRequest) {
        log.info("Generating exchange rates from Internal Database.");
        return getExchangeRate(exchangeRateRequest);
    }

    private Mono<ExchangeRateResponse> getExchangeRate(final ExchangeRateRequest exchangeRateRequest) {
        return exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(
                        exchangeRateRequest.getSourceCurrency(), exchangeRateRequest.getTargetCurrency())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(CURRENCY_NOT_FOUND_MESSAGE)))
                .map(exchangeRate -> BuilderUtil.buildExchangeRateResponse(exchangeRateRequest, exchangeRate.getRate(),
                        UUID.randomUUID().toString()));
    }
}