package com.has_to_be.csms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;


@Value
public class RateQueryDTO {

    @JsonProperty("overall")
    private BigDecimal overall;

    @JsonProperty("components")
    private RateComponentDTO rateComponentDTO;

}
