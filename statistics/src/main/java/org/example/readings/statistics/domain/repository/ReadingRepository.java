package org.example.readings.statistics.domain.repository;

import org.example.readings.statistics.domain.Reading;

import java.util.List;

public interface ReadingRepository {

    void save(Reading reading);

    List<Reading> findReadingsByCategoryAndBetween(String category, long from, long until);

    List<Reading> findReadingsByTypeAndCategoryAndBetween(String type, String category, long from, long until);
}
