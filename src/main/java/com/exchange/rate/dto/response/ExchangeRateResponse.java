package com.exchange.rate.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class ExchangeRateResponse {

    private String exchangeRateId;

    private BigDecimal amount;

    private String sourceCurrency;

    private String targetCurrency;

    private BigDecimal exchangedRate;

    private BigDecimal exchangedAmount;
}