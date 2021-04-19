package org.example.readings.iotdevices.domain;

import org.example.readings.domain.ReadingType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class Car extends IotDevice {

    private final String owner;
    private final String brand;

    public Car(String sensorId, String owner, String brand) {
        super(sensorId);
        this.owner = owner;
        this.brand = brand;
    }

    public Reading takeReading() {
        BigDecimal value = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0.00d, 50.00d))
                .setScale(2, RoundingMode.HALF_UP);
        return new Reading(getSensorId(), ReadingType.FUEL, ReadingType.FUEL.getCategory(), value, System.currentTimeMillis());
    }

    public String getOwner() {
        return owner;
    }

    public String getBrand() {
        return brand;
    }
}
