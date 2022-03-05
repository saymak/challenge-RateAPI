package com.has_to_be.csms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.math.BigDecimal;

/**
 * Returned as response body dto of /rate API
 */
@Value
public class RateQueryDTO {

    /**
     * overall cost of charging process rounded 2 decimal places
     */
    @JsonProperty("overall")
    @Schema(example = "7.04", description = "overall cost of charging process rounded 2 decimal places")
    private BigDecimal overall;

    /**
     * rate components serialized as "components" object include calculated cost details of charging process
     */
    @JsonProperty("components")
    private RateComponentDTO rateComponentDTO;

}

