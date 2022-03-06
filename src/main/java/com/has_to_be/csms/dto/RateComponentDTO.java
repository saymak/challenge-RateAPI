package com.has_to_be.csms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.math.BigDecimal;

/**
 * The {@link RateComponentDTO} class that includes Rating result details.
 */
@Value
public class RateComponentDTO {

    /**
     * charging process cost applied on CDR based on the energy consumed rounded 3 decimal places
     */
    @Schema(example = "3.277",
            description = "charging process cost based on the energy consumed rounded 3 decimal places")
    private BigDecimal energy;

    /**
     * charging process cost applied on CDR based on its time rounded 3 decimal places
     */
    @Schema(example = "2.767",
            description = "charging process cost based on its time rounded 3 decimal places")
    private BigDecimal time;

    /**
     * charging process cost applied on CDR based on ties fees per charging process rounded 3 decimal places
     */
    @Schema(example = "1",
            description = " charging process cost applied on CDR based on ties fees per charging process rounded 3 decimal places")
    private BigDecimal transaction;

}
