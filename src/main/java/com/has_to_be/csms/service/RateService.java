package com.has_to_be.csms.service;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateQueryDTO;


/**
 * The interface Rate service.
 */
public interface RateService {

    /**
     * Apply rate rate query dto.
     *
     * @param applyRateCommandDTO dto of a rate request details
     * @return RateQueryDTO dto of a rete calculation result result
     */
    RateQueryDTO applyRate(ApplyRateCommandDTO applyRateCommandDTO);

}
