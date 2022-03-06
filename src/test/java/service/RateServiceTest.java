package service;

import com.has_to_be.csms.dto.ApplyRateCommandDTO;
import com.has_to_be.csms.dto.CdrDTO;
import com.has_to_be.csms.dto.RateDTO;
import com.has_to_be.csms.dto.RateQueryDTO;
import com.has_to_be.csms.exception.InvalidApplyRateCommandException;
import com.has_to_be.csms.exception.MismatchedStartAndEndMeterException;
import com.has_to_be.csms.exception.MismatchedStartAndEndTimesException;
import com.has_to_be.csms.service.RateService;
import com.has_to_be.csms.service.RateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


class RateServiceTest {

    private RateService underTest = new RateServiceImpl();

    @Test
    void applyRate_applyRateCommandDTOWithValidCdrDTOAndRateDTO_returnsValidRateQueryDTO() {
        BigDecimal givenEnergy = BigDecimal.valueOf(0.3);
        BigDecimal givenTime = BigDecimal.valueOf(2);
        BigDecimal givenTransaction = BigDecimal.valueOf(1);
        RateDTO givenRateStub = new RateDTO(givenEnergy, givenTime, givenTransaction);
        String givenStartDateStub = "2021-04-05T10:04:00Z";
        String givenStopDateStub = "2021-04-05T11:27:00Z";
        Long givenMeterStartStub = 1_204_307L;
        Long givenMeterStopStub = 1_215_230L;
        CdrDTO givenCDRStub = new CdrDTO(givenMeterStartStub, givenMeterStopStub, givenStartDateStub, givenStopDateStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRateStub, givenCDRStub);

        RateQueryDTO rateQueryDTO = underTest.applyRate(givenRateCommand);

        Assertions.assertEquals(rateQueryDTO.getOverall(), BigDecimal.valueOf(7.04));
        Assertions.assertEquals(rateQueryDTO.getRateComponentDTO().getEnergy(), BigDecimal.valueOf(3.277));
        Assertions.assertEquals(rateQueryDTO.getRateComponentDTO().getTime(), BigDecimal.valueOf(2.767));
        Assertions.assertEquals(rateQueryDTO.getRateComponentDTO().getTransaction(), BigDecimal.valueOf(1));
    }

    @Test
    void applyRate_applyRateCommandDTOInvalidTimestampStart_throwsStartOrEndTimestampMismatchException() {
        BigDecimal givenEnergy = BigDecimal.valueOf(0.3);
        BigDecimal givenTime = BigDecimal.valueOf(2);
        BigDecimal givenTransaction = BigDecimal.valueOf(1);
        RateDTO givenRateStub = new RateDTO(givenEnergy, givenTime, givenTransaction);
        String givenStartDateStub = "2021-04-05T11:27:00Z";
        String givenStopDateStub = "2021-04-05T10:04:00Z";
        Long givenMeterStartStub = 1_204_307L;
        Long givenMeterStopStub = 1_215_230L;
        CdrDTO givenCDRStub = new CdrDTO(givenMeterStartStub, givenMeterStopStub, givenStartDateStub, givenStopDateStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRateStub, givenCDRStub);

        assertThrows(MismatchedStartAndEndTimesException.class, () -> underTest.applyRate(givenRateCommand));
    }

    @Test
    void applyRate_nullApplyRateCommandDTO_throwsInvalidApplyRateCommandException() {
        assertThrows(InvalidApplyRateCommandException.class, () -> underTest.applyRate(null));
    }

    @Test
    void applyRate_invalidStartStopMeter_throwsMismatchedStartAndEndMeterException() {
        Long givenMeterStartStub = 1_215_230L;
        Long givenMeterStopStub = 1_204_307L;
        RateDTO givenRate = Mockito.mock(RateDTO.class);
        CdrDTO givenCDR = Mockito.mock(CdrDTO.class);
        when(givenCDR.getMeterStart()).thenReturn(givenMeterStartStub);
        when(givenCDR.getMeterStop()).thenReturn(givenMeterStopStub);
        ApplyRateCommandDTO mockedApplyRateCommandDTO = new ApplyRateCommandDTO(givenRate, givenCDR);

        assertThrows(MismatchedStartAndEndMeterException.class, () -> underTest.applyRate(mockedApplyRateCommandDTO));
    }

}
