package com.has_to_be.csms.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.anyOf;
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

    @Autowired
    private MockMvc mockMvc;

    private static final String validTimestampStartStub = "2021-04-05T10:04:00Z";

    private static final String validTimestampEndStub = "2021-04-05T11:27:00Z";


    @MockBean
    private RateService rateService;

    @Test
    void applyRate_jsonObjectMissingCdrOrRateAsRequestBody_badRequestRateOrCdrJsonObjectIsMissing() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}".getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message",
                        anyOf(equalTo("rate object is missing"),
                                equalTo("cdr object is missing"))))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
    }


    @Test
    void applyRate_energyPropertyOfRateObjectIsMissing_badRequestRateOrCdrJsonObjectIsMissing() throws Exception {
        RateDTO rateDTO = new RateDTO(null, BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO cdrDTO = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("energy property of rate object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingEnergyPropertyOfRateObject_badRequestRateOrCdrJsonObjectIsMissing() throws Exception {
        RateDTO rateDTO = new RateDTO(null, BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO cdrDTO = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("energy property of rate object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTimePropertyOfRateObject_badRequestRateOrCdrJsonObjectIsMissing() throws Exception {
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(1), null, BigDecimal.valueOf(1));
        CdrDTO cdrDTO = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("time property of rate object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }


    @Test
    void applyRate_jsonMissingTransactionPropertyOfRateObject_badRequestRateOrCdrJsonObjectIsMissing()
            throws Exception {
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), null);
        CdrDTO cdrDTO = new CdrDTO(1L, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("transaction property of rate object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }


    @Test
    void applyRate_jsonMissingMeterStartPropertyOfCdrObject_badRequestRateOrCdrJsonObjectIsMissing()
            throws Exception {
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO cdrDTO = new CdrDTO(null, 1L, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("meterStart property of cdr object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTimestampStartPropertyOfCdrObject_badRequestRateOrCdrJsonObjectIsMissing()
            throws Exception {
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO cdrDTO = new CdrDTO(1L, 1L, null, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message", equalTo("timestampStart property of cdr object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }

    @Test
    void applyRate_jsonMissingTimestampStopPropertyOfCdrObject_badRequestRateOrCdrJsonObjectIsMissing()
            throws Exception {
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO cdrDTO = new CdrDTO(1L, 1L, validTimestampStartStub, null);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message",
                        equalTo("timestampStop property of cdr object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }


    @Test
    void applyRate_jsonMissingMeterStopPropertyOfCdrObject_badRequestRateOrCdrJsonObjectIsMissing()
            throws Exception {
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        CdrDTO cdrDTO =
                new CdrDTO(1L, null, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8))).andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message",
                        equalTo("meterStop property of cdr object is missing")))
                .andExpect(jsonPath("$.path", equalTo(ENDPOINT)))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.method", equalTo(HttpMethod.POST.name())))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(rateService);
    }


    @Test
    void applyRate_jsonObjectWithValidRateAndCdrObject_returnsChargingOverAllAndDetailsCosts() throws Exception {
        RateComponentDTO rateComponentDTOStub =
                new RateComponentDTO(BigDecimal.valueOf(3.277), BigDecimal.valueOf(2.767), BigDecimal.valueOf(1));
        RateQueryDTO rateQueryDTOStub =
                new RateQueryDTO(BigDecimal.valueOf(7.04), rateComponentDTOStub);
        when(rateService.applyRate(Mockito.any(ApplyRateCommandDTO.class))).thenReturn(rateQueryDTOStub);
        Long meterStartStub = 1_204_307L;
        Long meterStopStub = 1_215_230L;
        RateDTO rateDTO = new RateDTO(BigDecimal.valueOf(0.30), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
        CdrDTO cdrDTO =
                new CdrDTO(meterStartStub, meterStopStub, validTimestampStartStub, validTimestampEndStub);
        ApplyRateCommandDTO applyRateCommandDTO = new ApplyRateCommandDTO(rateDTO, cdrDTO);
        byte[] requestBodyStub = asJsonString(applyRateCommandDTO).getBytes(StandardCharsets.UTF_8);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBodyStub)).andDo(print())
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

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}