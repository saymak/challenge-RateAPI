package com.has_to_be.csms.controller;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateQueryDTO;
import com.has_to_be.csms.service.RateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class RateRestController {

    private final RateService rateService;

    public RateRestController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping("/rate")
    public RateQueryDTO applyRate(@Valid @RequestBody ApplyRateCommandDTO applyRateCommandDTO) {
        return rateService.applyRate(applyRateCommandDTO);
    }

}
