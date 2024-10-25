package com.exchange.rate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ExternalRestResponse {

    private String id;

    private BigDecimal rate;
}