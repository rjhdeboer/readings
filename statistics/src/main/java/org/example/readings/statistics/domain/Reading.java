package org.example.readings.statistics.domain;

import java.math.BigDecimal;

public class Reading {

    private final String sensorId;
    private final String type;
    private final String category;
    private final BigDecimal value;
    private final long takenAt;

    public Reading(String sensorId, String type, String category, BigDecimal value, long takenAt) {
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
}
