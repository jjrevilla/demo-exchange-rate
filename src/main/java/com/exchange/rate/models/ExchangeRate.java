package com.exchange.rate.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.exchange.rate.util.Constants.EXCHANGE_RATE_TABLE_NAME;

@Entity
@Getter
@Setter
@Table(name = EXCHANGE_RATE_TABLE_NAME)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceCurrency;

    private String targetCurrency;

    private BigDecimal rate;
}