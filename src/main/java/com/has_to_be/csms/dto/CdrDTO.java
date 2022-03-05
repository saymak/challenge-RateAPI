package com.has_to_be.csms.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Value
public class CdrDTO {

    @NotNull(message = "NotNull.meterStart.CdrDTO")
    @Min(value = 0, message = "Min.meterStart.CdrDTO")
    private Long meterStart;

    @NotNull(message = "NotNull.meterStop.CdrDTO")
    @Min(value = 0, message = "Min.meterStop.CdrDTO")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Long meterStop;

    @NotNull(message = "NotNull.timestampStart.CdrDTO")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String timestampStart;

    @NotNull(message = "NotNull.timestampStop.CdrDTO")
    private String timestampStop;

}
