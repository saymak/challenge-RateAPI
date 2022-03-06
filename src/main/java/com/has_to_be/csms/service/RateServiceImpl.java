package com.has_to_be.csms.service;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.RateComponentDTO;
import com.has_to_be.csms.dto.RateQueryDTO;
import com.has_to_be.csms.exception.InvalidApplyRateCommandException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.has_to_be.csms.util.RateUtil.*;
import static java.util.Objects.isNull;

@Service
public class RateServiceImpl implements RateService {

    @Override
    public RateQueryDTO applyRate(ApplyRateCommandDTO rateCommand) {
        if (isNull(rateCommand)) {
            throw new InvalidApplyRateCommandException();
        }

        BigDecimal energyCost = calculateEnergyCost(rateCommand.getCdr().getMeterStart(),
                rateCommand.getCdr().getMeterStop(),
                rateCommand.getRate().getEnergy(), DEFAULT_ROUNDING_MODE);
        BigDecimal timeCost = calculateTimeCost(rateCommand.getCdr().getTimestampStart(),
                rateCommand.getCdr().getTimestampStop(),
                rateCommand.getRate().getTime(), DEFAULT_ROUNDING_MODE);
        RateComponentDTO rateComponent = new RateComponentDTO(energyCost,
                timeCost, rateCommand.getRate().getTransaction());

        return new RateQueryDTO(energyCost.add(timeCost)
                .add(rateCommand.getRate().getTransaction())
                .setScale(2, DEFAULT_ROUNDING_MODE),
                rateComponent);
    }

}
