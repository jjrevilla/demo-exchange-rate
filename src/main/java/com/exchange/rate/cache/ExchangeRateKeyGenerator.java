package com.exchange.rate.cache;

import com.exchange.rate.dto.request.ExchangeRateRequest;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.exchange.rate.util.Constants.EXCHANGE_RATE_KEY_GENERATOR;

@Component(EXCHANGE_RATE_KEY_GENERATOR)
public class ExchangeRateKeyGenerator implements KeyGenerator {

    private static final String SEPARATOR = "::";

    private static final String CACHE_KEY = "exchangeRateKey";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        final ExchangeRateRequest exchangeRateRequest = (ExchangeRateRequest) params[0];
        return CACHE_KEY +
                SEPARATOR +
                exchangeRateRequest.getSourceCurrency() +
                SEPARATOR +
                exchangeRateRequest.getTargetCurrency() +
                SEPARATOR +
                exchangeRateRequest.getAmount();
    }
}