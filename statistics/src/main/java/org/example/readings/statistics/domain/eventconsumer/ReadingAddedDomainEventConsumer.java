package org.example.readings.statistics.domain.eventconsumer;

import org.example.readings.domain.events.ReadingAddedEvent;
import org.example.readings.statistics.domain.Reading;
import org.example.readings.statistics.domain.repository.ReadingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReadingAddedDomainEventConsumer implements DomainEventConsumer<ReadingAddedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ReadingAddedDomainEventConsumer.class);

    @Autowired
    private ReadingRepository repository;

    @Override
    public void consume(ReadingAddedEvent event) {
        logger.info("Received event: " + event);
        repository.save(new Reading(event.getSensorId(), event.getType(), event.getCategory(), event.getValue(), event.getTakenAt()));
    }
}
