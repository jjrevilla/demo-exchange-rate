package com.exchange.rate.repository;

import com.exchange.rate.models.ExchangeRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, Long> {

    Mono<ExchangeRate> findBySourceCurrencyAndTargetCurrency(final String sourceCurrency, final String targetCurrency);
}