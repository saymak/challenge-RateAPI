package com.has_to_be.csms.util;

import com.has_to_be.csms.exception.ISO8601DateFormatException;
import com.has_to_be.csms.exception.MismatchedStartAndEndMeterException;
import com.has_to_be.csms.exception.MismatchedStartAndEndTimesException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class RateUtil {

    private static final float KILO = 1_000F;

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private RateUtil() {
    }

    public static BigDecimal getHoursBetweenDateTimes(String startTimestamp, String stopTimestamp) {
        Date startDate = parseISO8601Date(startTimestamp);
        Date stopDate = parseISO8601Date(stopTimestamp);

        if (startDate.toInstant().isAfter(stopDate.toInstant())) {
            throw new MismatchedStartAndEndTimesException();
        }

        float timeDiff = ChronoUnit.MINUTES.between(startDate.toInstant(), stopDate.toInstant()) / 60f;
        return BigDecimal.valueOf(timeDiff);
    }

    public static BigDecimal calculateEnergyCost(Long meterStart, Long meterStop, BigDecimal energyCostPerKwh,
                                                 RoundingMode roundingMode) {
        if (meterStart > meterStop) {
            throw new MismatchedStartAndEndMeterException();
        }

        float consumedEnergyKWh = (meterStop - meterStart) / KILO;
        return energyCostPerKwh.multiply(BigDecimal.valueOf(consumedEnergyKWh)).setScale(3, roundingMode);
    }

    public static BigDecimal calculateTimeCost(String timestampStart, String timestampStop, BigDecimal costPerHour,
                                               RoundingMode roundingMode) {
        return costPerHour.multiply(getHoursBetweenDateTimes(timestampStart, timestampStop))
                .setScale(3, roundingMode);
    }

    public static Date parseISO8601Date(String stringDate) {
        try {
            return Date.from(ZonedDateTime.parse(stringDate, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant());
        } catch (Exception e) {
            throw new ISO8601DateFormatException(e);
        }
    }
}
