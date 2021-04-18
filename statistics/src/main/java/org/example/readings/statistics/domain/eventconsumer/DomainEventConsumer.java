package org.example.readings.statistics.domain.eventconsumer;

import org.example.readings.domain.events.DomainEvent;

public interface DomainEventConsumer<T extends DomainEvent> {

    void consume(T event);
}
