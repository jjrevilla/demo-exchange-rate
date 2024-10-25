package com.exchange.rate.dto.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ErrorDetails {

    private String code;

    private String message;

    private List<String> details;
}