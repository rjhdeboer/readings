package org.example.readings.statistics.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.readings.domain.events.DomainEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JacksonKafkaDeserializer implements Deserializer<DomainEvent> {

    private final ObjectMapper mapper;

    public JacksonKafkaDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public DomainEvent deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, DomainEvent.class);
        } catch (IOException | RuntimeException e) {
            throw new SerializationException("Error deserializing from JSON with Jackson", e);
        }
    }
}
