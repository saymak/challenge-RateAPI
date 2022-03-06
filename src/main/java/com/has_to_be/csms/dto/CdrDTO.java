package com.has_to_be.csms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * The {@link CdrDTO} class that includes CDR (Charging Data Record) details.
 */
@Value
public class CdrDTO {

    /**
     * meter value of the electricity meter when the charging process started
     */
    @NotNull(message = "NotNull.meterStart.CdrDTO")
    @Min(value = 0, message = "Min.meterStart.CdrDTO")
    @Schema(example = "1204307", description = "electricity meter start value")
    private Long meterStart;

    /**
     * meter value of the electricity meter when the charging process stopped
     */
    @NotNull(message = "NotNull.meterStop.CdrDTO")
    @Min(value = 0, message = "Min.meterStop.CdrDTO")
    @Schema(example = "1215230", description = "electricity meter stop value")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Long meterStop;

    /**
     * timestamp (according to ISO 8601) when the charging process started
     */
    @NotNull(message = "NotNull.timestampStart.CdrDTO")
    @Schema(example = "2021-04-05T10:04:00Z", description = "process start in timestamp")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String timestampStart;

    /**
     * timestamp (according to ISO 8601) when the charging process stopped
     */
    @NotNull(message = "NotNull.timestampStop.CdrDTO")
    @Schema(example = "2021-04-05T11:27:00Z", description = "process stop in timestamp")
    private String timestampStop;

}
