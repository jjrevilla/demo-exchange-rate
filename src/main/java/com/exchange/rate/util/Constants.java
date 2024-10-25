package com.exchange.rate.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String EXCHANGE_RATE_PATH = "exchange-rate";
    public static final String CURRENCY_PATTERN = "^[A-Z]{3}$";
    public static final String CURRENCY_PATTERN_MESSAGE = "Currency value doest not match the expected format.";
    public static final String AMOUNT_MESSAGE = "Amount value must be greater than zero.";
    public static final String AMOUNT_VALUE_MESSAGE = "Amount value doest not match the expected format.";
    public static final String ERROR_REST_MESSAGE = "Error calling external service.";
    public static final String ERROR_PARSE_MESSAGE = "Error parsing response from external service.";

    public static final String INTERNAL_ERROR_CODE = "E001";
    public static final String INTERNAL_ERROR_MESSAGE = "An unexpected error occurred.";
    public static final String BAD_INPUT_CODE = "E002";
    public static final String BAD_INPUT_MESSAGE = "Validation failed for input received.";
    public static final String EXTERNAL_ERROR_CODE = "E003";
    public static final String EXTERNAL_ERROR_MESSAGE = "External service for exchange rate is not available.";
}