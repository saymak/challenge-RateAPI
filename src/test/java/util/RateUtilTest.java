package util;

import com.has_to_be.csms.exception.ISO8601DateFormatException;
import com.has_to_be.csms.exception.MismatchedStartAndEndMeterException;
import com.has_to_be.csms.exception.MismatchedStartAndEndTimesException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.has_to_be.csms.util.RateUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class RateUtilTest {

    @Test
    void getHoursBetweenDateTimes_validStartAndEndTimeInISO8601_returnsHoursWithDecimalPoint() {
        String givenStartDateStub = "2021-04-05T10:04:00Z";
        String givenStopDateStub = "2021-04-05T11:27:00Z";

        BigDecimal result = getHoursBetweenDateTimes(givenStartDateStub,
                givenStopDateStub);

        assertEquals(result.setScale(3, DEFAULT_ROUNDING_MODE),
                BigDecimal.valueOf(1.383));
    }

    @Test
    void getHoursBetweenDateTimes_invalidStarEndTimeInISO8601_throwsDateMismatchedStartEndOrFormatException() {
        String givenStartDateStub = "2021-04-05T11:27:00Z";
        String givenStopDateStub = "2021-04-05T10:04:00Z";

        assertThrows(MismatchedStartAndEndTimesException.class,
                () -> getHoursBetweenDateTimes(givenStartDateStub, givenStopDateStub));
    }

    @Test
    void calculateEnergyCost_validStartAndStopMeter_shouldReturnElectricityCodeWithAppliedRate() {
        Long giveMeterStartStub = 1_204_307L;
        Long givenMeterStopStub = 1_215_230L;
        BigDecimal givenEnergyCostStub = BigDecimal.valueOf(0.30);

        BigDecimal result = calculateEnergyCost(giveMeterStartStub, givenMeterStopStub,
                givenEnergyCostStub, DEFAULT_ROUNDING_MODE);

        assertEquals(result, BigDecimal.valueOf(3.277));
    }

    @Test
    void calculateEnergyCost_inValidStartAndStopMeter_throwsMismatchedStartAndEndMeterException() {
        Long giveMeterStartStub = 1_215_230L;
        Long givenMeterStopStub = 1_204_307L;
        BigDecimal givenEnergyCostStub = BigDecimal.valueOf(0.30);

        assertThrows(MismatchedStartAndEndMeterException.class, () ->
                calculateEnergyCost(giveMeterStartStub, givenMeterStopStub, givenEnergyCostStub,
                        DEFAULT_ROUNDING_MODE));
    }

    @Test
    void calculateTimeCost_invalidStarEndTimeInISO8601_throwsMismatchedStartAndEndTimesException() {
        String givenStartDateStub = "2021-04-05T11:27:00Z";
        String givenStopDateStub = "2021-04-05T10:04:00Z";

        assertThrows(MismatchedStartAndEndTimesException.class, () ->
                getHoursBetweenDateTimes(givenStartDateStub, givenStopDateStub));
    }

    @Test
    void parseISO8601Date_validISO8601DateInString_doesNotThrowException() {
        String givenDateStub = "2021-04-05T11:27:00Z";

        assertDoesNotThrow(() -> parseISO8601Date(givenDateStub));
    }

    @Test
    void parseISO8601Date_iso8601DateInStringMissingZ_doesNotThrowException() {
        String iso8601ValidStub = "2021-04-05T11:27:00";

        assertThrows(ISO8601DateFormatException.class, () -> parseISO8601Date(iso8601ValidStub));
    }

    @Test
    void parseISO8601Date_iso8601DateInStringMissingT_doesNotThrowException() {
        String iso8601ValidStub = "2021-04-0511:27:00Z";

        assertThrows(ISO8601DateFormatException.class, () -> parseISO8601Date(iso8601ValidStub));
    }

    @Test
    void calculateTimeCost_validStartEndTime_returnValidTimeCostWith3DecimalPlaces() {
        String givenStartDateStub = "2021-04-05T10:04:00Z";
        String givenStopDateStub = "2021-04-05T11:27:00Z";

        BigDecimal result = calculateTimeCost(givenStartDateStub, givenStopDateStub, BigDecimal.valueOf(2),
                DEFAULT_ROUNDING_MODE);

        assertEquals(result.setScale(3,
                DEFAULT_ROUNDING_MODE), BigDecimal.valueOf(2.767));
    }

    @Test
    void calculateTimeCost_invalidStartEndTime_throwsMismatchedStartAndEndTimesException() {
        String givenStartDateStub = "2021-04-05T11:27:00Z";
        String givenStopDateStub = "2021-04-05T10:04:00Z";
        BigDecimal givenTimeCostStub = BigDecimal.valueOf(0);

        assertThrows(MismatchedStartAndEndTimesException.class,
                () -> calculateTimeCost(givenStartDateStub,
                        givenStopDateStub,
                        givenTimeCostStub, DEFAULT_ROUNDING_MODE));
    }

    @Test
    void parseISO8601Date_invalidISO8601DataInString_throwsISO8601DateFormatException() {
        String iso8601ValidStub = "2021-04-0511:27:00Z";
        assertThrows(ISO8601DateFormatException.class, () -> parseISO8601Date(iso8601ValidStub));
    }

}

