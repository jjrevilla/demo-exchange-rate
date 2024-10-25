package com.exchange.rate.controllers.services.impl;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.exceptions.ResourceNotFoundException;
import com.exchange.rate.models.ExchangeRate;
import com.exchange.rate.repository.ExchangeRateRepository;
import com.exchange.rate.services.impl.DefaultExchangeRateServiceImpl;
import com.exchange.rate.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.exchange.rate.util.Constants.CURRENCY_NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultExchangeRateServiceImplTest {

    @Mock
    ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    DefaultExchangeRateServiceImpl defaultExchangeRateService;

    private static ExchangeRateRequest exchangeRateRequest;

    @BeforeAll
    static void setup() {
        exchangeRateRequest = TestUtil.buildExchangeRateRequest();
    }

    @Test
    @DisplayName("return exchangeRateResponse when request exchangeRate")
    void returnExchangeRateResponseWhenRequestExchangeRate() {

        final ExchangeRate exchangeRate = TestUtil.buildExchangeRate();
        final BigDecimal expectedExchangeRate = exchangeRateRequest.getAmount().multiply(exchangeRate.getRate())
                .setScale(2, RoundingMode.HALF_UP);

        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(anyString(), anyString()))
                .thenReturn(Mono.just(exchangeRate));

        StepVerifier.create(defaultExchangeRateService.exchangeRate(exchangeRateRequest))
                .consumeNextWith(result -> {
                    assertEquals(exchangeRateRequest.getAmount(), result.getAmount());
                    assertEquals(exchangeRateRequest.getSourceCurrency(), result.getSourceCurrency());
                    assertEquals(exchangeRateRequest.getTargetCurrency(), result.getTargetCurrency());
                    assertEquals(exchangeRate.getRate(), result.getExchangedRate());
                    assertEquals(expectedExchangeRate, result.getExchangedAmount());
                    assertNotNull(result.getExchangeRateId());
                })
                .verifyComplete();

        verify(exchangeRateRepository, times(1)).findBySourceCurrencyAndTargetCurrency(anyString(), anyString());
    }

    @Test
    @DisplayName("return exception when exchange rate is not found")
    void returnExceptionWhenExchangeRateIsNotFound() {

        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(anyString(), anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(defaultExchangeRateService.exchangeRate(exchangeRateRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResourceNotFoundException
                                && CURRENCY_NOT_FOUND_MESSAGE.equals(throwable.getMessage()))
                .verify();

        verify(exchangeRateRepository, times(1)).findBySourceCurrencyAndTargetCurrency(anyString(), anyString());
    }
}