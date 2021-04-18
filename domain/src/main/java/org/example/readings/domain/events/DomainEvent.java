package org.example.readings.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReadingAddedEvent.class, name = "ReadingAdded")
})
public abstract class DomainEvent {

    private final UUID id;
    private final String version;

    public
    DomainEvent(String version) {
        this.id = UUID.randomUUID();
        this.version = version;
    }

    protected DomainEvent(UUID id, String version) {
        this.id = id;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }
}
