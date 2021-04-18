package org.example.readings.iotdevices.domain;

import org.example.readings.domain.ReadingCategory;
import org.example.readings.domain.ReadingType;

import java.math.BigDecimal;

public class Reading {

    private final ReadingType type;
    private final ReadingCategory category;
    private final BigDecimal value;
    private final long takenAt;

    public Reading(ReadingType type, ReadingCategory category, BigDecimal value, long takenAt) {
        this.type = type;
        this.category = category;
        this.value = value;
        this.takenAt = takenAt;
    }

    public ReadingType getType() {
        return type;
    }

    public ReadingCategory getCategory() {
        return category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public long getTakenAt() {
        return takenAt;
    }
}
