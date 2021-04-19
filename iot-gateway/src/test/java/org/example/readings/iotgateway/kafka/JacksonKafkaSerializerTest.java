package org.example.readings.iotgateway.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.example.readings.domain.events.ReadingAddedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JacksonKafkaSerializerTest {

    private JacksonKafkaSerializer<ReadingAddedEvent> serializer;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serializer = new JacksonKafkaSerializer<>(objectMapper);
    }

    @Test
    void shouldNotSerializeNullData() throws Exception {
        byte[] serializedValue = serializer.serialize("readings", null);

        assertNull(serializedValue);
    }

    @Test
    void shouldSerializeValidEvent() throws Exception {
        ReadingAddedEvent event = new ReadingAddedEvent("car_1", "1", "fuel", "transportation", BigDecimal.TEN, 1L);
        byte[] expectedResult = new byte[0];

        when(objectMapper.writeValueAsBytes(event)).thenReturn(expectedResult);

        byte[] serializedValue = serializer.serialize("readings", event);

        assertEquals(expectedResult, serializedValue);
    }

    @Test
    void shouldNotSerializeOnExceptionThrown() throws Exception {
        ReadingAddedEvent event = new ReadingAddedEvent("car_1", "1", "fuel", "transportation", BigDecimal.TEN, 1L);

        when(objectMapper.writeValueAsBytes(event)).thenThrow(new RuntimeException());

        assertThrows(SerializationException.class, () -> serializer.serialize("readings", event));
    }
}
