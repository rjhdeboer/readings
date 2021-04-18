package org.example.readings.statistics.domain.repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.example.readings.statistics.domain.Reading;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryReadingRepository implements ReadingRepository {

    private final ListMultimap<String, Reading> readings;

    public InMemoryReadingRepository() {
        this(ArrayListMultimap.create());
    }

    InMemoryReadingRepository(ListMultimap<String, Reading> readings) {
        this.readings = readings;
    }

    public void save(Reading reading) {
        readings.put(reading.getCategory(), reading);
    }

    @Override
    public List<Reading> findReadingsByCategoryAndBetween(String category, long from, long until) {
        List<Reading> readingsByCategory = this.readings.get(category);
        return readingsByCategory.stream()
                .filter(reading -> reading.getTakenAt() >= from)
                .filter(reading -> reading.getTakenAt() <= until)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Reading> findReadingsByTypeAndCategoryAndBetween(String type, String category, long from, long until) {
        return findReadingsByCategoryAndBetween(category, from, until).stream()
                .filter(reading -> reading.getType().equals(type))
                .collect(Collectors.toUnmodifiableList());
    }
}
