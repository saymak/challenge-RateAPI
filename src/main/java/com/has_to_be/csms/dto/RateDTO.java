package com.has_to_be.csms.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Value
public class RateDTO {

    @NotNull(message = "NotNull.energy.RateDTO")
    @Min(value = 0, message = "Min.energy.RateDTO")
    private BigDecimal energy;

    @NotNull(message = "NotNull.time.RateDTO")
    @Min(value = 0, message = "Min.time.RateDTO")
    private BigDecimal time;

    @NotNull(message = "NotNull.transaction.RateDTO")
    @Min(value = 0, message = "Min.transaction.RateDTO")
    private BigDecimal transaction;

}
