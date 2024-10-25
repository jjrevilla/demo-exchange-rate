package com.exchange.rate.util;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import com.exchange.rate.dto.response.ExchangeRateResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuilderUtil {

    public static ExchangeRateResponse buildExchangeRateResponse(final ExchangeRateRequest exchangeRateRequest,
                                                                 final BigDecimal exchangeRate,
                                                                 final String exchangeRateId) {
        return ExchangeRateResponse.builder()
                .exchangeRateId(exchangeRateId)
                .amount(exchangeRateRequest.getAmount())
                .sourceCurrency(exchangeRateRequest.getSourceCurrency())
                .targetCurrency(exchangeRateRequest.getTargetCurrency())
                .exchangedRate(exchangeRate)
                .exchangedAmount(computeExchangeAmount(exchangeRateRequest.getAmount(), exchangeRate))
                .build();
    }

    private static BigDecimal computeExchangeAmount(final BigDecimal amount, final BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}