package org.example.readings.statistics.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.example.readings.domain.events.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class JacksonKafkaDeserializerTest {

    private JacksonKafkaDeserializer deserializer;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deserializer = new JacksonKafkaDeserializer(objectMapper);
    }

    @Test
    void shouldDeserializeValidData() throws Exception {
        byte[] serializedData = new byte[0];
        DomainEvent expectedResult = deserializer.deserialize("readings", serializedData);

        when(objectMapper.readValue(serializedData, DomainEvent.class)).thenReturn(expectedResult);

        DomainEvent event = deserializer.deserialize("readings", serializedData);

        assertEquals(expectedResult, event);
    }

    @Test
    void shouldNotDeserializeOnExceptionThrown() throws Exception {
        byte[] serializedData = new byte[0];

        when(objectMapper.readValue(serializedData, DomainEvent.class)).thenThrow(new RuntimeException());

        assertThrows(SerializationException.class, () -> deserializer.deserialize("readings", serializedData));
    }
}
