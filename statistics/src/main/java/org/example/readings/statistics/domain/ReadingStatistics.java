package org.example.readings.statistics.domain;

import java.math.BigDecimal;

public class ReadingStatistics {

    private final BigDecimal min;
    private final BigDecimal max;
    private final BigDecimal average;
    private final BigDecimal median;

    public ReadingStatistics(BigDecimal min, BigDecimal max, BigDecimal average, BigDecimal median) {
        this.min = min;
        this.max = max;
        this.average = average;
        this.median = median;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public BigDecimal getMedian() {
        return median;
    }
}
