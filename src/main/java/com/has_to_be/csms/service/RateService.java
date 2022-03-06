package com.has_to_be.csms.service;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateQueryDTO;

public interface RateService {

    /**
     * Apply supplied rates on rating request.
     *
     * @param applyRateCommandDTO dto of a rate request details
     * @return {@link RateQueryDTO}
     */
    RateQueryDTO applyRate(ApplyRateCommandDTO applyRateCommandDTO);

}
