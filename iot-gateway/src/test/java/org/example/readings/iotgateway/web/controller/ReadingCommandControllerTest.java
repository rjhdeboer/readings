package org.example.readings.iotgateway.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.example.readings.domain.events.DomainEvent;
import org.example.readings.iotgateway.domain.AddReadingCommand;
import org.example.readings.iotgateway.domain.processor.CommandProcessor;
import org.example.readings.iotgateway.web.dto.ReadingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "kafka.createTopics=false")
@AutoConfigureMockMvc
public class ReadingCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommandProcessor<AddReadingCommand> commandProcessor;
    @MockBean
    private KafkaProducer<UUID, DomainEvent> producer;

    @Test
    public void shouldAcceptValidReading() throws Exception {
        ReadingDto reading = new ReadingDto("car_1", "fuel", "transportation", BigDecimal.TEN, 1L);

        this.mockMvc.perform(
                post("/readings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(reading)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRejectInvalidReading() throws Exception {
        verifyInvalidReadingRequest(new ReadingDto(null, "fuel", "transportation", BigDecimal.TEN, 1L));
        verifyInvalidReadingRequest(new ReadingDto("car_1", null, "transportation", BigDecimal.TEN, 1L));
        verifyInvalidReadingRequest(new ReadingDto("car_1", "fuel", null, BigDecimal.TEN, 1L));
        verifyInvalidReadingRequest(new ReadingDto("car_1", "fuel", "transportation", null, 1L));
        verifyInvalidReadingRequest(new ReadingDto("car_1", "fuel", "transportation", BigDecimal.TEN, -1L));
    }

    private void verifyInvalidReadingRequest(ReadingDto reading) throws Exception {
        this.mockMvc.perform(
                post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reading)))
                .andExpect(status().isBadRequest());
    }
}
