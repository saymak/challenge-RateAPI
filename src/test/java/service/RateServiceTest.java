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

    @Test
    void applyRate_applyRateCommandDTOWithValidCdrDTOAndRateDTO_returnsValidRateQueryDTO() throws Exception {
        RateService rateService = new RateServiceImpl();
        BigDecimal energy = BigDecimal.valueOf(0.3);
        BigDecimal time = BigDecimal.valueOf(2);
        BigDecimal transaction = BigDecimal.valueOf(1);
        RateDTO rateDTOStub = new RateDTO(energy, time, transaction);
        String timestampStartStub = "2021-04-05T10:04:00Z";
        String timestampStopStub = "2021-04-05T11:27:00Z";
        Long meterStartStub = 1_204_307L;
        Long meterStopStub = 1_215_230L;
        CdrDTO cdrDTOStub = new CdrDTO(meterStartStub, meterStopStub, timestampStartStub, timestampStopStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTOStub, cdrDTOStub);
        RateQueryDTO rateQueryDTO = rateService.applyRate(applyRateCommandDTO);
        Assertions.assertEquals(rateQueryDTO.getOverall(), BigDecimal.valueOf(7.04));
        Assertions.assertEquals(rateQueryDTO.getRateComponentDTO().getEnergy(), BigDecimal.valueOf(3.277));
        Assertions.assertEquals(rateQueryDTO.getRateComponentDTO().getTime(), BigDecimal.valueOf(2.767));
        Assertions.assertEquals(rateQueryDTO.getRateComponentDTO().getTransaction(), BigDecimal.valueOf(1));
    }

    @Test
    void applyRate_applyRateCommandDTOInvalidTimestampStart_throwsStartOrEndTimestampMismatchException() throws Exception {
        RateService rateService = new RateServiceImpl();
        BigDecimal energy = BigDecimal.valueOf(0.3);
        BigDecimal time = BigDecimal.valueOf(2);
        BigDecimal transaction = BigDecimal.valueOf(1);
        RateDTO rateDTOStub = new RateDTO(energy, time, transaction);
        String timestampStartStub = "2021-04-05T11:27:00Z";
        String timestampStopStub = "2021-04-05T10:04:00Z";
        Long meterStartStub = 1_204_307L;
        Long meterStopStub = 1_215_230L;
        CdrDTO cdrDTOStub = new CdrDTO(meterStartStub, meterStopStub, timestampStartStub, timestampStopStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTOStub, cdrDTOStub);
        assertThrows(MismatchedStartAndEndTimesException.class, () -> rateService.applyRate(applyRateCommandDTO));
    }

    @Test
    void applyRate_nullApplyRateCommandDTO_throwsInvalidApplyRateCommandException() throws Exception {
        RateService rateService = new RateServiceImpl();
        assertThrows(InvalidApplyRateCommandException.class, () -> rateService.applyRate(null));
    }

    @Test
    void applyRate_invalidStartStopMeter_throwsMismatchedStartAndEndMeterException() throws Exception {
        // meterStopStub is less than meterStartStub
        Long meterStartStub = 1_215_230L;
        Long meterStopStub = 1_204_307L;
        CdrDTO mockedCdrDTO = Mockito.mock(CdrDTO.class);
        RateDTO mockedRateDTO = Mockito.mock(RateDTO.class);
        when(mockedCdrDTO.getMeterStart()).thenReturn(meterStartStub);
        when(mockedCdrDTO.getMeterStop()).thenReturn(meterStopStub);
        ApplyRateCommandDTO mockedApplyRateCommandDTO = new ApplyRateCommandDTO(mockedRateDTO, mockedCdrDTO);
        RateService rateService = new RateServiceImpl();
        assertThrows(MismatchedStartAndEndMeterException.class, () -> rateService.applyRate(mockedApplyRateCommandDTO));
    }

}