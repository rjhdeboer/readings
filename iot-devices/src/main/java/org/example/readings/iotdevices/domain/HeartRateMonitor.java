package org.example.readings.iotdevices.domain;

import org.example.readings.domain.ReadingType;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class HeartRateMonitor extends IotDevice {

    public Reading takeReading() {
        BigDecimal value = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(60, 100));
        return new Reading(ReadingType.HEART_RATE, ReadingType.HEART_RATE.getCategory(), value, System.currentTimeMillis());
    }
}
