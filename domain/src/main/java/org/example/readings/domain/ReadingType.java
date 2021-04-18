package org.example.readings.domain;

import static org.example.readings.domain.ReadingCategory.*;

public enum ReadingType {
    TEMPERATURE(ENVIRONMENTAL),
    HEART_RATE(MEDICAL),
    FUEL(TRANSPORTATION);

    private final ReadingCategory category;

    ReadingType(ReadingCategory category) {
        this.category = category;
    }

    public ReadingCategory getCategory() {
        return category;
    }
}
