package org.example.readings.iotgateway.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public class JacksonKafkaSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper;

    public JacksonKafkaSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }

        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException | RuntimeException e) {
            throw new SerializationException("Error serializing to JSON with Jackson", e);
        }
    }
}
