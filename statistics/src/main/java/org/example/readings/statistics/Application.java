package org.example.readings.statistics;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.example.readings.domain.events.DomainEvent;
import org.example.readings.domain.events.ReadingAddedEvent;
import org.example.readings.statistics.domain.eventconsumer.DomainEventConsumer;
import org.example.readings.statistics.kafka.JacksonKafkaDeserializer;
import org.example.readings.statistics.web.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import static org.example.readings.statistics.web.security.Role.ROLE_ANALIST;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public KafkaConsumer<UUID, DomainEvent> createConsumer(JacksonKafkaDeserializer jacksonKafkaDeserializer)
            throws Exception {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:19091");
        // NOTE: for partition balancing a fixed group id needs to be set. Due to the in-memory repository, this group id
        // is now set to a random value so that all instances of statistics will read all partitions
        //config.put(ConsumerConfig.GROUP_ID_CONFIG, "statistics");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        // make sure that all events from the beginning of time are read on each startup due to in-memory repository
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(config, new UUIDDeserializer(), jacksonKafkaDeserializer);
    }

    @Bean
    public CommandLineRunner onStartup(KafkaConsumer<UUID, DomainEvent> consumer,
                                       DomainEventConsumer<ReadingAddedEvent> eventConsumer,
                                       JwtTokenProvider jwtTokenProvider) {
        String token = jwtTokenProvider.createToken("analist", ROLE_ANALIST);
        logger.info("*** AUTHENTICATION TOKEN: {} ***", token);

        consumer.subscribe(Collections.singletonList("readings"));

        return args -> {
            try (consumer) {
                do {
                    ConsumerRecords<UUID, DomainEvent> records = consumer.poll(Duration.ofSeconds(1));
                    for (ConsumerRecord<UUID, DomainEvent> record : records) {
                        eventConsumer.consume((ReadingAddedEvent) record.value());
                    }
                } while (true);
            }
        };
    }
}
