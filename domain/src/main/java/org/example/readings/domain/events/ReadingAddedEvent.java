package org.example.readings.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadingAddedEvent extends DomainEvent {

    private final String sensorId;
    private final String type;
    private final String category;
    private final BigDecimal value;
    private final long takenAt;

    @JsonCreator
    public ReadingAddedEvent(@JsonProperty("id") UUID id,
                             @JsonProperty("sensorId") String sensorId,
                             @JsonProperty("version") String version,
                             @JsonProperty("type") String type,
                             @JsonProperty("category") String category,
                             @JsonProperty("value") BigDecimal value,
                             @JsonProperty("takenAt") long takenAt) {
        super(id, version);
        this.sensorId = sensorId;
        this.type = type;
        this.category = category;
        this.value = value;
        this.takenAt = takenAt;
    }

    public ReadingAddedEvent(String version, String sensorId, String type, String category, BigDecimal value, long takenAt) {
        super(version);
        this.sensorId = sensorId;
        this.type = type;
        this.category = category;
        this.value = value;
        this.takenAt = takenAt;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public long getTakenAt() {
        return takenAt;
    }

    @Override
    public String toString() {
        return "ReadingAddedEvent{" +
                "sensorId='" + sensorId + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", takenAt=" + takenAt +
                '}';
    }
}
