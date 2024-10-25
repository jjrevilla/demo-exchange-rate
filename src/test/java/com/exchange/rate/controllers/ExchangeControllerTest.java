package com.exchange.rate.controllers;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.exceptions.ExternalRestException;
import com.exchange.rate.services.ExchangeRateService;
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

import static com.exchange.rate.util.TestUtil.AMOUNT;
import static com.exchange.rate.util.TestUtil.INTERNAL_ERROR_MESSAGE;
import static com.exchange.rate.util.TestUtil.PEN;
import static com.exchange.rate.util.TestUtil.USD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    private static ExchangeRateRequest exchangeRateRequest;

    private static ExchangeRateResponse exchangeRateResponse;

    @BeforeAll
    static void setup() {
        exchangeRateRequest = TestUtil.buildExchangeRateRequest();
        exchangeRateResponse = TestUtil.buildExchangeRateResponse();
    }

    @Test
    @DisplayName("return exchangeRateResponse when request exchangeRate")
    void returnExchangeRateResponseWhenRequestExchangeRate() {

        when(exchangeRateService.exchangeRate(any())).thenReturn(Mono.just(exchangeRateResponse));

        StepVerifier.create(exchangeRateController.getExchangeRate(exchangeRateRequest))
                .consumeNextWith(response -> {
                    assertEquals(PEN, response.getSourceCurrency());
                    assertEquals(USD, response.getTargetCurrency());
                    assertEquals(AMOUNT, response.getAmount());
                    assertNotNull(response.getExchangeRateId());
                    assertNotNull(response.getExchangedAmount());
                })
                .verifyComplete();

        verify(exchangeRateService, times(1)).exchangeRate(any());
    }

    @Test
    @DisplayName("return exception when error is thrown on request exchangeRate")
    void returnExceptionWhenErrorIsThrownOnRequestExchangeRate() {

        when(exchangeRateService.exchangeRate(any()))
                .thenReturn(Mono.error(new ExternalRestException(INTERNAL_ERROR_MESSAGE)));

        StepVerifier.create(exchangeRateController.getExchangeRate(exchangeRateRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof ExternalRestException
                                && INTERNAL_ERROR_MESSAGE.equals(throwable.getMessage()))
                .verify();

        verify(exchangeRateService, times(1)).exchangeRate(any());
    }
}