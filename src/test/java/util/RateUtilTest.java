package util;

import com.has_to_be.csms.exception.ISO8601DateFormatException;
import com.has_to_be.csms.exception.MismatchedStartAndEndMeterException;
import com.has_to_be.csms.exception.MismatchedStartAndEndTimesException;
import com.has_to_be.csms.util.RateUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RateUtilTest {

    @Test
    void getHoursBetweenDateTimes_validStartAndEndTimeInISO8601_returnsHoursWithDecimalPoint()
            throws Exception {
        String timestampStartStub = "2021-04-05T10:04:00Z";
        String timestampStopStub = "2021-04-05T11:27:00Z";
        BigDecimal hoursBetweenDateTimes = RateUtil.getHoursBetweenDateTimes(timestampStartStub,
                timestampStopStub);
        assertEquals(hoursBetweenDateTimes.setScale(3, RateUtil.DEFAULT_ROUNDING_MODE),
                BigDecimal.valueOf(1.383));
    }

    @Test
    void getHoursBetweenDateTimes_invalidStarEndTimeInISO8601_throwsDateMismatchedStartEndOrFormatException()
            throws Exception {
        // timestampStartStub and timestampStopStub are in invalid order
        String timestampStartStub = "2021-04-05T11:27:00Z";
        String timestampStopStub = "2021-04-05T10:04:00Z";
        assertThrows(MismatchedStartAndEndTimesException.class, () -> RateUtil
                .getHoursBetweenDateTimes(timestampStartStub, timestampStopStub));
    }

    @Test
    void calculateEnergyCost_validStartAndStopMeter_shouldReturnElectricityCodeWithAppliedRate()
            throws Exception {
        Long meterStartStub = 1_204_307L;
        Long meterStopStub = 1_215_230L;
        BigDecimal electricityCostRateStub = BigDecimal.valueOf(0.30);
        BigDecimal energyCostWith3DecimalPlaces =
                RateUtil.calculateEnergyCost(meterStartStub, meterStopStub,
                        electricityCostRateStub, RateUtil.DEFAULT_ROUNDING_MODE);
        assertEquals(energyCostWith3DecimalPlaces, BigDecimal.valueOf(3.277));
    }

    @Test
    void calculateEnergyCost_inValidStartAndStopMeter_throwsMismatchedStartAndEndMeterException()
            throws Exception {
        // meterStartStub and meterStopStub are in invalid order
        Long meterStartStub = 1_215_230L;
        Long meterStopStub = 1_204_307L;
        BigDecimal electricityCostRateStub = BigDecimal.valueOf(0.30);
        assertThrows(MismatchedStartAndEndMeterException.class, () -> RateUtil.
                calculateEnergyCost(meterStartStub, meterStopStub, electricityCostRateStub,
                        RateUtil.DEFAULT_ROUNDING_MODE));
    }

    @Test
    void calculateTimeCost_invalidStarEndTimeInISO8601_throwsMismatchedStartAndEndTimesException()
            throws Exception {

        // timestampStartStub timestampStopStub are in invalid order
        String timestampStartStub = "2021-04-05T11:27:00Z";
        String timestampStopStub = "2021-04-05T10:04:00Z";
        assertThrows(MismatchedStartAndEndTimesException.class, () -> RateUtil.
                getHoursBetweenDateTimes(timestampStartStub, timestampStopStub));
    }

    @Test
    void parseISO8601Date_validISO8601DataInString_returnsValidDateObject() throws Exception {
        String iso8601ValidStub = "2021-04-05T11:27:00Z";
        Date date = RateUtil.parseISO8601Date(iso8601ValidStub);
        assertNotNull(date);
    }

    @Test
    void calculateTimeCost_validStartEndTime_returnValidTimeCostWith3DecimalPlaces() throws Exception {
        String timestampStartStub = "2021-04-05T10:04:00Z";
        String timestampStopStub = "2021-04-05T11:27:00Z";
        BigDecimal calculateTimeCost = RateUtil
                .calculateTimeCost(timestampStartStub, timestampStopStub, BigDecimal.valueOf(2),
                        RateUtil.DEFAULT_ROUNDING_MODE);
        assertEquals(calculateTimeCost.setScale(3,
                RateUtil.DEFAULT_ROUNDING_MODE), BigDecimal.valueOf(2.767));
    }

    @Test
    void calculateTimeCost_invalidStartEndTime_throwsMismatchedStartAndEndTimesException() throws Exception {
        // timestampStartStub timestampStopStub are in invalid order
        String timestampStartStub = "2021-04-05T11:27:00Z";
        String timestampStopStub = "2021-04-05T10:04:00Z";
        // just an unused stub to pass as argument
        BigDecimal costPerHourStub = BigDecimal.valueOf(0);
        assertThrows(MismatchedStartAndEndTimesException.class, () -> RateUtil.
                calculateTimeCost(timestampStartStub,
                        timestampStopStub,
                        costPerHourStub, RateUtil.DEFAULT_ROUNDING_MODE));
    }

    @Test
    void parseISO8601Date_invalidISO8601DataInString_throwsISO8601DateFormatException() throws Exception {
        // string date is not valid in iso 8601 format. T is missing
        String iso8601ValidStub = "2021-04-0511:27:00Z";
        assertThrows(ISO8601DateFormatException.class, () -> RateUtil.parseISO8601Date(iso8601ValidStub));
    }

}
