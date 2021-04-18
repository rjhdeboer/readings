package org.example.readings.statistics.domain.eventconsumer;

import org.example.readings.domain.events.ReadingAddedEvent;
import org.example.readings.statistics.domain.Reading;
import org.example.readings.statistics.domain.repository.ReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class ReadingAddedDomainEventConsumerTest {

    @InjectMocks
    private ReadingAddedDomainEventConsumer consumer;
    @Mock
    private ReadingRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldConsumeEvent() {
        ReadingAddedEvent event = new ReadingAddedEvent("1", "fuel", "transportation", BigDecimal.TEN, 1L);

        consumer.consume(event);

        ArgumentCaptor<Reading> readingCaptor = ArgumentCaptor.forClass(Reading.class);
        verify(repository).save(readingCaptor.capture());
        Reading reading = readingCaptor.getValue();
        assertEquals(event.getType(), reading.getType());
        assertEquals(event.getCategory(), reading.getCategory());
        assertEquals(event.getValue(), reading.getValue());
        assertEquals(event.getTakenAt(), reading.getTakenAt());
    }
}