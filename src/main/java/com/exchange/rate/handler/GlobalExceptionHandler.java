package com.exchange.rate.handler;

import com.exchange.rate.dto.error.ErrorDetails;
import com.exchange.rate.exceptions.ExternalRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import static com.exchange.rate.util.Constants.BAD_INPUT_CODE;
import static com.exchange.rate.util.Constants.BAD_INPUT_MESSAGE;
import static com.exchange.rate.util.Constants.ERROR_PARSE_MESSAGE;
import static com.exchange.rate.util.Constants.EXTERNAL_ERROR_CODE;
import static com.exchange.rate.util.Constants.EXTERNAL_ERROR_MESSAGE;
import static com.exchange.rate.util.Constants.INTERNAL_ERROR_CODE;
import static com.exchange.rate.util.Constants.INTERNAL_ERROR_MESSAGE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> handleWebExchangeBindException(final WebExchangeBindException ex) {
        log.error(ex.getMessage(), ex);
        final ErrorDetails errorDetails = ErrorDetails.builder()
                .code(BAD_INPUT_CODE)
                .message(BAD_INPUT_MESSAGE)
                .details(ex.getFieldErrors().stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .toList())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalRestException.class)
    public ResponseEntity<?> handleExternalRestException(final ExternalRestException ex) {
        log.error(ex.getMessage(), ex);
        final boolean errorParse = ex.getCause() != null;
        final ErrorDetails errorDetails = ErrorDetails.builder()
                .code(EXTERNAL_ERROR_CODE)
                .message(errorParse ? ERROR_PARSE_MESSAGE : EXTERNAL_ERROR_MESSAGE)
                .build();
        return new ResponseEntity<>(errorDetails,
                errorParse ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(final Exception ex) {
        log.error(ex.getMessage(), ex);
        final ErrorDetails errorDetails =
                ErrorDetails.builder().code(INTERNAL_ERROR_CODE).message(INTERNAL_ERROR_MESSAGE).build();
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}