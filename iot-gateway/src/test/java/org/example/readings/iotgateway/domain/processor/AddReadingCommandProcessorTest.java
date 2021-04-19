package org.example.readings.iotgateway.domain.processor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.readings.domain.events.DomainEvent;
import org.example.readings.domain.events.ReadingAddedEvent;
import org.example.readings.iotgateway.domain.AddReadingCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class AddReadingCommandProcessorTest {

    @InjectMocks
    private AddReadingCommandProcessor commandProcessor;
    @Mock
    private KafkaProducer<UUID, DomainEvent> producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldProcessCommand() {
        AddReadingCommand command = new AddReadingCommand("car_1", "fuel", "transportation", BigDecimal.TEN, 1L);

        commandProcessor.process(command);

        ArgumentCaptor<ProducerRecord> producerRecordCaptor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(producer).send(producerRecordCaptor.capture());
        ProducerRecord producerRecord = producerRecordCaptor.getValue();
        assertEquals("readings", producerRecord.topic());
        ReadingAddedEvent event = (ReadingAddedEvent) producerRecord.value();
        assertNotNull(event.getId());
        assertEquals("1", event.getVersion());
        assertEquals(command.getSensorId(), event.getSensorId());
        assertEquals(command.getType(), event.getType());
        assertEquals(command.getCategory(), event.getCategory());
        assertEquals(command.getValue(), event.getValue());
        assertEquals(command.getTakenAt(), event.getTakenAt());
    }
}
