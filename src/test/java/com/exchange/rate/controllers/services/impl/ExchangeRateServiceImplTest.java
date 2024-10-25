package com.exchange.rate.controllers.services.impl;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.dto.response.ExternalRestResponse;
import com.exchange.rate.services.DefaultExchangeRateService;
import com.exchange.rate.services.impl.ExchangeRateServiceImpl;
import com.exchange.rate.util.TestUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

import static com.exchange.rate.util.Constants.CIRCUIT_BREAKER_EXCHANGE_RATE_NAME;
import static com.exchange.rate.util.TestUtil.EXCHANGE_RATE;
import static com.exchange.rate.util.TestUtil.EXCHANGE_RATE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceImplTest {

    @Mock
    private DefaultExchangeRateService defaultExchangeRateService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    private ExchangeRateRequest exchangeRateRequest;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    public void setUp() {
        exchangeRateRequest = TestUtil.buildExchangeRateRequest();

        final CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_EXCHANGE_RATE_NAME);
    }

    @Test
    @DisplayName("return exchangeRateResponse when circuit breaker is closed")
    void returnExchangeRateResponseWhenCircuitBreakerIsClosed() {

        final ExternalRestResponse externalRestResponse = new ExternalRestResponse(EXCHANGE_RATE_ID, EXCHANGE_RATE);
        final BigDecimal expectedExchangeRate = exchangeRateRequest.getAmount().multiply(externalRestResponse.getRate())
                .setScale(2, RoundingMode.HALF_UP);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(ExternalRestResponse.class))).thenReturn(Mono.just(externalRestResponse));

        assertSame(circuitBreaker.getState(), CircuitBreaker.State.CLOSED);

        StepVerifier.create(exchangeRateService.exchangeRate(exchangeRateRequest))
                .consumeNextWith(result -> {
                    assertInstanceOf(ExchangeRateResponse.class, result);
                    assertEquals(exchangeRateRequest.getAmount(), result.getAmount());
                    assertEquals(exchangeRateRequest.getSourceCurrency(), result.getSourceCurrency());
                    assertEquals(exchangeRateRequest.getTargetCurrency(), result.getTargetCurrency());
                    assertEquals(externalRestResponse.getRate(), result.getExchangedRate());
                    assertEquals(expectedExchangeRate, result.getExchangedAmount());
                    assertNotNull(result.getExchangeRateId());
                })
                .verifyComplete();

        verify(defaultExchangeRateService, times(0)).exchangeRate(any());
    }
}