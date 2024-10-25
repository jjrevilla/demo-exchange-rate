package com.exchange.rate.exceptions;

public class ExternalRestException extends RuntimeException {
    public ExternalRestException(final String message) {
        super(message);
    }

    public ExternalRestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}