package com.has_to_be.csms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.has_to_be.csms.configuration.MessageSourceConfiguration;
import com.has_to_be.csms.dto.*;
import com.has_to_be.csms.service.RateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateRestController.class)
@Import({MessageSourceConfiguration.class})
@ExtendWith(SpringExtension.class)
class RateRestControllerTest {

    private static final String ENDPOINT = "/rate";

    private final String validTimestampStartStub = "2021-04-05T10:04:00Z";

    private final String validTimestampEndStub = "2021-04-05T11:27:00Z";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RateService rateService;

    @Test
    void applyRate_jsonObjectMissingCdrAsRequestBody_returnsBadRequestCdrObjectIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(0.1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, null);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("cdr is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void applyRate_jsonObjectMissingRateAsRequestBody_returnsBadRequestRateObjectIsMissing() throws Exception {
        CdrDTO givenCDR = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(null, givenCDR);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("rate is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void applyRate_energyOfRateObjectIsMissing_returnsBadRequestEnergyOfRateObjectIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(null, BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO givenCDR = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("energy of rate is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTimeOfRateObject_returnsBadRequestTimeOfRateIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(1), null, BigDecimal.valueOf(1));
        CdrDTO givenCDR = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("time of rate is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTransactionOfRateObject_returnsBadRequestTransactionOfRateIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), null);
        CdrDTO givenCDR = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("transaction of rate is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }


    @Test
    void applyRate_jsonMissingMeterStartOfCdrObject_returnsBadRequestMeterStartOfCDRIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO givenCDR = new CdrDTO(null, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("meter start of cdr is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTimestampStartOfCdrObject_returnsBadRequestTimestampStartOfCDRIsMissing()
            throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO givenCDR = new CdrDTO(1L, 1L, null, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message",
                        equalTo("start time of cdr is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTimestampStopOfCdrObject_returnsBadRequestTimestampStopOfCDRIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO givenCDR = new CdrDTO(1L, 1L, validTimestampStartStub, null);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message",
                        equalTo("stop time of cdr is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingMeterStopOfCdrObject_returnsBadRequestMeterStopOfCDRIsMissing() throws Exception {
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO givenCDR =
                new CdrDTO(1L, null, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message",
                        equalTo("meter stop of cdr is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonObjectWithValidRateAndCdrObject_returnsChargingOverAllAndCostDetails() throws Exception {
        Long givenMeterStart = 1_204_307L;
        Long givenMeterStop = 1_215_230L;
        RateDTO givenRate = new RateDTO(BigDecimal.valueOf(0.30), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
        CdrDTO givenCDR = new CdrDTO(givenMeterStart, givenMeterStop, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO givenRateCommand = new ApplyRateCommandDTO(givenRate, givenCDR);
        byte[] givenRequestBodyStub = asJsonString(givenRateCommand).getBytes(StandardCharsets.UTF_8);

        RateComponentDTO givenRateComponentStub =
                new RateComponentDTO(BigDecimal.valueOf(3.277), BigDecimal.valueOf(2.767), BigDecimal.valueOf(1));
        RateQueryDTO rateQueryStub = new RateQueryDTO(BigDecimal.valueOf(7.04), givenRateComponentStub);
        when(rateService.applyRate(Mockito.any(ApplyRateCommandDTO.class))).thenReturn(rateQueryStub);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(givenRequestBodyStub)).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.overall", equalTo(7.04)))
                .andExpect(jsonPath("$.components").isNotEmpty())
                .andExpect(jsonPath("$.components.energy", equalTo(3.277)))
                .andExpect(jsonPath("$.components.time", equalTo(2.767)))
                .andExpect(jsonPath("$.components.transaction", equalTo(1)))
                .andExpect(status().isOk());
        Mockito.verify(rateService, Mockito.times(1))
                .applyRate(Mockito.any(ApplyRateCommandDTO.class));
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
