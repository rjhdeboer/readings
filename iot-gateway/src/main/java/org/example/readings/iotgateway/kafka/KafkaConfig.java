package org.example.readings.iotgateway.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {

    @Value("${kafka.bootstrapServers}")
    private String bootstrapServers;
    @Value("${kafka.consumers.topics.readings.name}")
    private String readingsTopic;
    @Value("${kafka.consumers.topics.readings.partitions}")
    private int partitions;
    @Value("${kafka.replicationFactor}")
    private short replicationFactor;
    @Value("${kafka.createTopics}")
    private boolean createTopics;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getReadingsTopic() {
        return readingsTopic;
    }

    public int getPartitions() {
        return partitions;
    }

    public short getReplicationFactor() {
        return replicationFactor;
    }

    public boolean isCreateTopics() {
        return createTopics;
    }
}
