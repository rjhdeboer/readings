package org.example.readings.statistics.domain.processor;

import org.example.readings.statistics.domain.FetchReadingStatisticsQuery;
import org.example.readings.statistics.domain.Reading;
import org.example.readings.statistics.domain.ReadingStatistics;
import org.example.readings.statistics.domain.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FetchReadingStatisticsQueryProcessor implements QueryProcessor<FetchReadingStatisticsQuery, ReadingStatistics> {

    @Autowired
    private ReadingRepository repository;

    @Override
    public ReadingStatistics handle(FetchReadingStatisticsQuery query) {
        List<Reading> readings = query.getType() != null ?
            repository.findReadingsByTypeAndCategoryAndBetween(query.getType(), query.getCategory(), query.getFrom(), query.getUntil()) :
            repository.findReadingsByCategoryAndBetween(query.getCategory(), query.getFrom(), query.getUntil());

        BigDecimal min = calculateMin(readings);
        BigDecimal max = calculateMax(readings);
        BigDecimal average = calculateAverage(readings);
        BigDecimal median = calculateMedian(readings);

        return new ReadingStatistics(min, max, average, median);
    }

    private BigDecimal calculateMin(List<Reading> readings) {
        return readings.stream()
                .map(Reading::getValue)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateMax(List<Reading> readings) {
        return readings.stream()
                .map(Reading::getValue)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateAverage(List<Reading> readings) {
        if (readings.size() == 0) {
            return BigDecimal.ZERO;
        }

        return readings.stream()
                .map(Reading::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(readings.size()), RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMedian(List<Reading> readings) {
        if (readings.size() == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal median;
        List<BigDecimal> readingsSortedByValue = readings.stream()
                .map(Reading::getValue)
                .sorted(BigDecimal::compareTo)
                .collect(Collectors.toList());

        if (readingsSortedByValue.size() % 2 == 1) {
            median = readingsSortedByValue.get(readingsSortedByValue.size() / 2);
        } else {
            BigDecimal intermediate = readingsSortedByValue.get((readingsSortedByValue.size() / 2) - 1);
            intermediate = intermediate.add(readingsSortedByValue.get(readingsSortedByValue.size() / 2));
            median = intermediate.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        }
        return median;
    }
}
