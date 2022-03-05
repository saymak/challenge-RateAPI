package com.has_to_be.csms.util;

import com.has_to_be.csms.exception.ISO8601DateFormatException;
import com.has_to_be.csms.exception.MismatchedStartAndEndMeterException;
import com.has_to_be.csms.exception.MismatchedStartAndEndTimesException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class RateUtil {
    private static final float KILO = 1_000F;

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private RateUtil() {
    }

    public static BigDecimal getHoursBetweenDateTimes(String timestampStartInISO8601, String timestampStopInISO8601) {
        Date timestampStart = RateUtil.parseISO8601Date(timestampStartInISO8601);
        Date timestampStop = RateUtil.parseISO8601Date(timestampStopInISO8601);
        if (timestampStart.toInstant().isAfter(timestampStop.toInstant())) {
            throw new MismatchedStartAndEndTimesException();
        } else {
            float timeDifferencesInHoursWithDecimalPlaces =
                    ChronoUnit.MINUTES.between(timestampStart.toInstant(), timestampStop.toInstant()) / 60f;
            return BigDecimal.valueOf(timeDifferencesInHoursWithDecimalPlaces);
        }
    }

    public static BigDecimal calculateEnergyCost(Long meterStart, Long meterStop,
                                                 BigDecimal energyCostPerKwh, RoundingMode roundingMode) {
        if (meterStart > meterStop) {
            throw new MismatchedStartAndEndMeterException();
        } else {
            float consumedEnergyInKilowattHour = (meterStop - meterStart) / KILO;
            return energyCostPerKwh
                    .multiply(BigDecimal.valueOf(consumedEnergyInKilowattHour))
                    .setScale(3, roundingMode);
        }
    }

    public static BigDecimal calculateTimeCost(String timestampStart,
                                               String timestampStop, BigDecimal costPerHour, RoundingMode roundingMode) {
        return costPerHour
                .multiply(getHoursBetweenDateTimes(timestampStart, timestampStop))
                .setScale(3, roundingMode);
    }

    public static Date parseISO8601Date(String stringDateISO8601) {
        try {
            return Date.from(ZonedDateTime.parse(stringDateISO8601).toInstant());
        } catch (Exception e) {
            throw new ISO8601DateFormatException(e);
        }
    }
}
