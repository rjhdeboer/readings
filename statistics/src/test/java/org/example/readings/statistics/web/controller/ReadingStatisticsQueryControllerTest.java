package org.example.readings.statistics.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.readings.domain.events.DomainEvent;
import org.example.readings.statistics.domain.FetchReadingStatisticsQuery;
import org.example.readings.statistics.domain.ReadingStatistics;
import org.example.readings.statistics.domain.processor.QueryProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReadingStatisticsQueryControllerTest {

    private static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmFsaXN0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMSVNUIn1dLCJpYXQiOjE2MTg2NDQyODN9.4hlDKWmXssVOs1Zj2Y_rSC5ls62PEQMj7YTBXSKDgxQ";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryProcessor<FetchReadingStatisticsQuery, ReadingStatistics> queryProcessor;
    @MockBean
    private KafkaConsumer<UUID, DomainEvent> consumer;
    @MockBean
    private CommandLineRunner commandLineRunner;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAcceptValidStatisticsRequestForTypeAndCategory() throws Exception {
        ReadingStatistics readingStatistics = new ReadingStatistics(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ONE);

        when(queryProcessor.handle(isA(FetchReadingStatisticsQuery.class))).thenReturn(readingStatistics);

        this.mockMvc.perform(
                buildRequestWithHeaders(JWT_TOKEN)
                    .param("type", "fuel")
                    .param("category", "transportation")
                    .param("from", "1")
                    .param("until", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(readingStatistics)));

        ArgumentCaptor<FetchReadingStatisticsQuery> queryCaptor = ArgumentCaptor.forClass(FetchReadingStatisticsQuery.class);
        verify(queryProcessor).handle(queryCaptor.capture());
        FetchReadingStatisticsQuery query = queryCaptor.getValue();
        assertEquals("fuel", query.getType());
        assertEquals("transportation", query.getCategory());
        assertEquals("1", String.valueOf(query.getFrom()));
        assertEquals("10", String.valueOf(query.getUntil()));
    }

    @Test
    public void shouldRejectStatisticsRequestWithoutValidToken() throws Exception {
        this.mockMvc.perform(
                buildRequestWithHeaders("Bearer xyz")
                        .param("type", "fuel")
                        .param("category", "transportation")
                        .param("from", "1")
                        .param("until", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldRejectStatisticsRequestForMissingTypeOrCategory() throws Exception {
        this.mockMvc.perform(
                buildRequestWithHeaders(JWT_TOKEN)
                    .param("from", "1")
                    .param("until", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectStatisticsRequestForMissingFrom() throws Exception {
        this.mockMvc.perform(
                buildRequestWithHeaders(JWT_TOKEN)
                        .param("type", "fuel")
                        .param("until", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectStatisticsRequestForMissingUntil() throws Exception {
        this.mockMvc.perform(
                buildRequestWithHeaders(JWT_TOKEN)
                        .param("type", "fuel")
                        .param("from", "1"))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder buildRequestWithHeaders(String jwtToken) {
        return get("/statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", jwtToken);
    }
}
