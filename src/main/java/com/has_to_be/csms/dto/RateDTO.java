package com.has_to_be.csms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * The {@link RateDTO} class that includes Rate details.
 */
@Value
public class RateDTO {

    /**
     * rate of charging process based on the energy consumed, per-kWh fee
     */
    @NotNull(message = "NotNull.energy.RateDTO")
    @Min(value = 0, message = "Min.energy.RateDTO")
    @Schema(example = "0.30", description = "rate the charging process based on the energy consumed, per kWh fee")
    private BigDecimal energy;

    /**
     * rate of charging process based on its duration, per-hour fee
     */
    @NotNull(message = "NotNull.time.RateDTO")
    @Min(value = 0, message = "Min.time.RateDTO")
    @Schema(example = "2", description = "rate the charging process based on its duration, per hour fee")
    private BigDecimal time;

    /**
     * rate of charging process, per-transaction fee
     */
    @NotNull(message = "NotNull.transaction.RateDTO")
    @Min(value = 0, message = "Min.transaction.RateDTO")
    @Schema(example = "1", description = "fees per charging process, per-transaction fee")
    private BigDecimal transaction;

}
