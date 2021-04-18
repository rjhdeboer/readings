package org.example.readings.iotgateway;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.example.readings.domain.events.DomainEvent;
import org.example.readings.iotgateway.kafka.JacksonKafkaSerializer;
import org.example.readings.iotgateway.kafka.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class Application {

    @Autowired
    private KafkaConfig kafkaConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(value = ApplicationStartedEvent.class, condition = "@kafkaConfig.isCreateTopics()")
    public void createKafkaTopicIfNeeded() throws Exception {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        AdminClient adminClient = KafkaAdminClient.create(properties);

        ListTopicsResult topics = adminClient.listTopics();
        Set<String> currentTopics = topics.names().get();
        if (!currentTopics.contains(kafkaConfig.getReadingsTopic())) {
            adminClient.createTopics(Collections.singletonList(new NewTopic(kafkaConfig.getReadingsTopic(),
                    kafkaConfig.getPartitions(), kafkaConfig.getReplicationFactor())));
        }
    }

    @Bean
    public KafkaProducer<UUID, DomainEvent> createProducer(JacksonKafkaSerializer<DomainEvent> jacksonKafkaSerializer)
            throws Exception {
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "iot-gateway-" + InetAddress.getLocalHost().getHostName());
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        return new KafkaProducer<>(config, new UUIDSerializer(), jacksonKafkaSerializer);
    }
}
