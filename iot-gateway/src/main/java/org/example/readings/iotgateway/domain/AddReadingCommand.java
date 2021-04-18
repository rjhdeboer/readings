package org.example.readings.iotgateway.domain;

import java.math.BigDecimal;

public class AddReadingCommand {

    private final String type;
    private final String category;
    private final BigDecimal value;
    private final long takenAt;

    public AddReadingCommand(String type, String category, BigDecimal value, long takenAt) {
        this.type = type;
        this.category = category;
        this.value = value;
        this.takenAt = takenAt;
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
