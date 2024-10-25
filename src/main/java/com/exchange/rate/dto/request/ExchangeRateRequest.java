package com.exchange.rate.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.exchange.rate.util.Constants.AMOUNT_MESSAGE;
import static com.exchange.rate.util.Constants.AMOUNT_VALUE_MESSAGE;
import static com.exchange.rate.util.Constants.CURRENCY_PATTERN;
import static com.exchange.rate.util.Constants.CURRENCY_PATTERN_MESSAGE;

@RequiredArgsConstructor
@Getter
@Setter
public class ExchangeRateRequest {

    @DecimalMin(value = "0.0", inclusive = false, message = AMOUNT_MESSAGE)
    @Digits(integer = 6, fraction = 2, message = AMOUNT_VALUE_MESSAGE)
    private BigDecimal amount;

    @Pattern(regexp = CURRENCY_PATTERN, message = CURRENCY_PATTERN_MESSAGE)
    private String sourceCurrency;

    @Pattern(regexp = CURRENCY_PATTERN, message = CURRENCY_PATTERN_MESSAGE)
    private String targetCurrency;
}