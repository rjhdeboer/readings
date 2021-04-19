package org.example.readings.statistics.domain.repository;

import com.google.common.collect.ArrayListMultimap;
import org.example.readings.statistics.domain.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryReadingRepositoryTest {

    private InMemoryReadingRepository repository;
    private ArrayListMultimap<String, Reading> readings;

    @BeforeEach
    public void setUp() {
        readings = ArrayListMultimap.create();
        repository = new InMemoryReadingRepository(readings);
    }

    @Test
    public void shouldSave() {
        Reading reading = new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 1L);

        repository.save(reading);

        assertTrue(readings.containsEntry(reading.getCategory(), reading));
    }

    @Test
    public void shouldFindReadingsByCategoryAndBetween() {
        Reading expectedFirstResult = new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 1L);
        Reading expectedSecondResult = new Reading("car_2", "fuel", "transportation", BigDecimal.TEN, 2L);
        readings.put("transportation", expectedFirstResult);
        readings.put("transportation", expectedSecondResult);
        readings.put("transportation", new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 3L));
        readings.put("medical", new Reading("monitor_1", "heartrate", "medical", new BigDecimal("60"), 1L));
        readings.put("medical", new Reading("monitor_1", "heartrate", "medical", new BigDecimal("70"), 2L));

        List<Reading> results = repository.findReadingsByCategoryAndBetween("transportation", 1L, 2L);
        assertEquals(2, results.size());
        assertTrue(results.contains(expectedFirstResult));
        assertTrue(results.contains(expectedSecondResult));
    }

    @Test
    public void shouldFindReadingsByTypeAndCategoryAndBetween() {
        Reading expectedFirstResult = new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 1L);
        Reading expectedSecondResult = new Reading("car_2", "fuel", "transportation", BigDecimal.TEN, 2L);
        readings.put("transportation", expectedFirstResult);
        readings.put("transportation", expectedSecondResult);
        readings.put("transportation", new Reading("car_1", "oil", "transportation", BigDecimal.TEN, 1L));
        readings.put("transportation", new Reading("car_2", "oil", "transportation", BigDecimal.TEN, 2L));
        readings.put("transportation", new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 3L));
        readings.put("medical", new Reading("monitor_1", "heartrate", "medical", new BigDecimal("60"), 1L));
        readings.put("medical", new Reading("monitor_1", "heartrate", "medical", new BigDecimal("70"), 2L));

        List<Reading> results = repository.findReadingsByTypeAndCategoryAndBetween("fuel", "transportation", 1L, 2L);
        assertEquals(2, results.size());
        assertTrue(results.contains(expectedFirstResult));
        assertTrue(results.contains(expectedSecondResult));
    }

    @Test
    public void shouldFindReadingsBySensorIdAndTypeBetween() {
        Reading expectedFirstResult = new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 1L);
        Reading expectedSecondResult = new Reading("car_2", "fuel", "transportation", BigDecimal.TEN, 2L);
        readings.put("transportation", expectedFirstResult);
        readings.put("transportation", expectedSecondResult);
        readings.put("transportation", new Reading("car_1", "oil", "transportation", BigDecimal.TEN, 1L));
        readings.put("transportation", new Reading("car_2", "oil", "transportation", BigDecimal.TEN, 2L));
        readings.put("transportation", new Reading("car_1", "fuel", "transportation", BigDecimal.TEN, 3L));
        readings.put("medical", new Reading("monitor_1", "heartrate", "medical", new BigDecimal("60"), 1L));
        readings.put("medical", new Reading("monitor_1", "heartrate", "medical", new BigDecimal("70"), 2L));

        List<Reading> results = repository.findReadingsBySensorIdAndTypeAndBetween("car_1", "fuel", 1L, 2L);
        assertEquals(1, results.size());
        assertTrue(results.contains(expectedFirstResult));
    }
}
