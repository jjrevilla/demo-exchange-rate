package com.exchange.rate.util;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import com.exchange.rate.models.ExchangeRate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtil {

    public final static String USD = "USD";
    public final static String PEN = "PEN";
    public final static BigDecimal AMOUNT = BigDecimal.valueOf(10.55);
    public final static BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(3.75);
    public final static String EXCHANGE_RATE_ID = UUID.randomUUID().toString();
    public static final String INTERNAL_ERROR_MESSAGE = "An unexpected error occurred.";

    public static ExchangeRateRequest buildExchangeRateRequest() {
        final ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest();
        exchangeRateRequest.setAmount(AMOUNT);
        exchangeRateRequest.setSourceCurrency(PEN);
        exchangeRateRequest.setTargetCurrency(USD);
        return exchangeRateRequest;
    }

    public static ExchangeRateResponse buildExchangeRateResponse() {
        return BuilderUtil.buildExchangeRateResponse(buildExchangeRateRequest(), EXCHANGE_RATE, EXCHANGE_RATE_ID);
    }

    public static ExchangeRate buildExchangeRate() {
        final ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(AMOUNT);
        exchangeRate.setSourceCurrency(PEN);
        exchangeRate.setSourceCurrency(USD);
        return exchangeRate;
    }
}