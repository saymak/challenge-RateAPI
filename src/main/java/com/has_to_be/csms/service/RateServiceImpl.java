package com.has_to_be.csms.service;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateComponentDTO;
import com.has_to_be.csms.dto.RateQueryDTO;
import com.has_to_be.csms.exception.InvalidApplyRateCommandException;
import com.has_to_be.csms.util.RateUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * The Rate service implementation.
 */
@Service
public class RateServiceImpl implements RateService {

    @Override
    public RateQueryDTO applyRate(ApplyRateCommandDTO applyRateCommandDTO) {
        if (applyRateCommandDTO == null) {
            throw new InvalidApplyRateCommandException();
        }
        // method should prepare 4 data elements:
        // 1- energy: electricity consumption cost in charging process with 3
        BigDecimal energyCost = RateUtil.calculateEnergyCost(applyRateCommandDTO.getCdrDTO().getMeterStart(),
                applyRateCommandDTO.getCdrDTO().getMeterStop(),
                applyRateCommandDTO.getRateDTO().getEnergy(), RateUtil.DEFAULT_ROUNDING_MODE);
        // 2- time: cost of period of time charging station were busy with 3 decimal places precision
        BigDecimal timeCost =
                RateUtil.calculateTimeCost(applyRateCommandDTO.getCdrDTO().getTimestampStart(),
                        applyRateCommandDTO.getCdrDTO().getTimestampStop(),
                        applyRateCommandDTO.getRateDTO().getTime(), RateUtil.DEFAULT_ROUNDING_MODE);
        // 3- transaction: each charging round has got a constant transaction fee with 3 decimal places precision
        // 4- overall: total amount of following cost with 2 decimal places precision
        RateComponentDTO rateComponentDTO =
                new RateComponentDTO(energyCost, timeCost, applyRateCommandDTO.getRateDTO().getTransaction());
        return new RateQueryDTO(
                energyCost
                        .add(timeCost)
                        .add(applyRateCommandDTO.getRateDTO().getTransaction())
                        .setScale(2, RateUtil.DEFAULT_ROUNDING_MODE),
                rateComponentDTO);
    }

}
