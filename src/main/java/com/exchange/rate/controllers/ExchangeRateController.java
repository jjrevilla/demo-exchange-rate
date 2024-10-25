package com.exchange.rate.controllers;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.services.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.exchange.rate.util.Constants.EXCHANGE_RATE_PATH;

@RestController
@RequestMapping(EXCHANGE_RATE_PATH)
@AllArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @PostMapping
    public Mono<ExchangeRateResponse> getExchangeRate(@Valid @RequestBody final ExchangeRateRequest request) {
        return exchangeRateService.exchangeRate(request);
    }
}