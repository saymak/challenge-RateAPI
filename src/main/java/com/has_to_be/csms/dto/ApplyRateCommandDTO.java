package com.has_to_be.csms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
public class ApplyRateCommandDTO {

    @Valid
    @NotNull(message = "NotNull.rateDTO.ApplyRateCommandDTO")
    @JsonProperty("rate")
    private RateDTO rateDTO;

    @Valid
    @NotNull(message = "NotNull.cdrDTO.ApplyRateCommandDTO")
    @JsonProperty("cdr")
    private CdrDTO cdrDTO;

}
