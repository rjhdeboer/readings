package org.example.readings.iotdevices.domain;

import org.example.readings.domain.ReadingType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class Thermostat extends IotDevice {

    public Reading takeReading() {
        BigDecimal value = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(18.0d, 22.0d))
                .setScale(2, RoundingMode.HALF_UP);
        return new Reading(ReadingType.TEMPERATURE, ReadingType.TEMPERATURE.getCategory(), value, System.currentTimeMillis());
    }
}
