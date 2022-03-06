package com.has_to_be.csms.controller;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateQueryDTO;
import com.has_to_be.csms.service.RateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * The Rate rest controller.
 */
@Tag(name = "Rate API", description = "Charging process Rating endpoint")
@RestController
public class RateRestController {

    private final RateService rateService;

    /**
     * {@link RateService} rateService Injected aas dependency of {@link RateRestController}
     */
    public RateRestController(RateService rateService) {
        this.rateService = rateService;
    }

    /**
     * This method handles post request on /rate endpoint.
     *
     * @param applyRateCommand {@link ApplyRateCommandDTO} contains request details
     * @return {@link RateQueryDTO} that includes one property "overall" and "component" object
     */
    @Operation(summary = "Calculate charging process costs based on details: rate and cdr ")
    @PostMapping("/rate")
    public RateQueryDTO applyRate(@Valid @RequestBody ApplyRateCommandDTO applyRateCommand) {
        return rateService.applyRate(applyRateCommand);
    }

}
