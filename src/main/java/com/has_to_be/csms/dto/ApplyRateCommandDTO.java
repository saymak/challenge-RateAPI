package com.has_to_be.csms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The {@link ApplyRateCommandDTO} class that includes Rate Request details.
 */
@Value
public class ApplyRateCommandDTO {

    @Valid
    @NotNull(message = "NotNull.rateDTO.ApplyRateCommandDTO")
    @JsonProperty("rate")
    private RateDTO rate;

    @Valid
    @NotNull(message = "NotNull.cdrDTO.ApplyRateCommandDTO")
    @JsonProperty("cdr")
    private CdrDTO cdr;

}

