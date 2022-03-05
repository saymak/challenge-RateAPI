package com.has_to_be.csms.service;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateQueryDTO;


public interface RateService {

    RateQueryDTO applyRate(ApplyRateCommandDTO applyRateCommandDTO);

}
