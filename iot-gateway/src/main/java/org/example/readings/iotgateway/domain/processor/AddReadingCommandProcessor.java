package org.example.readings.iotgateway.domain.processor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.readings.domain.events.DomainEvent;
import org.example.readings.domain.events.ReadingAddedEvent;
import org.example.readings.iotgateway.domain.AddReadingCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddReadingCommandProcessor implements CommandProcessor<AddReadingCommand> {

    @Autowired
    private KafkaProducer<UUID, DomainEvent> producer;

    @Override
    public void process(AddReadingCommand command) {
        // note that the command itself is not sent via Kafka, meaning that we potentially lose this command if the server
        // crashes before sending the event
        ReadingAddedEvent event = new ReadingAddedEvent("1", command.getType(), command.getCategory(), command.getValue(),
                command.getTakenAt());
        producer.send(new ProducerRecord<>("readings", event));
    }
}
