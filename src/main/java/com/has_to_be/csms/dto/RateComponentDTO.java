package com.has_to_be.csms.dto;

import lombok.Value;

import java.math.BigDecimal;


@Value
public class RateComponentDTO {

    private BigDecimal energy;

    private BigDecimal time;

    private BigDecimal transaction;

}
