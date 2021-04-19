package org.example.readings.statistics.domain.processor;

import org.example.readings.statistics.domain.FetchReadingStatisticsQuery;
import org.example.readings.statistics.domain.Reading;
import org.example.readings.statistics.domain.ReadingStatistics;
import org.example.readings.statistics.domain.repository.ReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FetchReadingStatisticsQueryProcessorTest {

    @InjectMocks
    private FetchReadingStatisticsQueryProcessor processor;
    @Mock
    private ReadingRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldHandleQueryWithTypeAndCategory() {
        FetchReadingStatisticsQuery query = new FetchReadingStatisticsQuery(null, "fuel", "transportation", 1L, 10L);
        List<Reading> readings = Arrays.asList(
            new Reading("car_1", "fuel", "transportation", new BigDecimal("1.00"), 1L),
            new Reading("car_1", "fuel", "transportation", new BigDecimal("2.00"), 2L),
            new Reading("car_1", "fuel", "transportation", new BigDecimal("3.00"), 3L),
            new Reading("car_1", "fuel", "transportation", new BigDecimal("4.00"), 4L),
            new Reading("car_1", "fuel", "transportation", new BigDecimal("5.00"), 5L)
        );

        when(repository.findReadingsByTypeAndCategoryAndBetween(query.getType(), query.getCategory(), query.getFrom(),
                query.getUntil())).thenReturn(readings);

        ReadingStatistics statistics = processor.handle(query);
        assertEquals(new BigDecimal("1.00"), statistics.getMin());
        assertEquals(new BigDecimal("5.00"), statistics.getMax());
        assertEquals(new BigDecimal("3.00"), statistics.getAverage());
        assertEquals(new BigDecimal("3.00"), statistics.getMedian());
    }

    @Test
    public void shouldHandleQueryWithSensorId() {
        FetchReadingStatisticsQuery query = new FetchReadingStatisticsQuery("car_1", null, null, 1L, 10L);
        List<Reading> readings = Arrays.asList(
                new Reading("car_1", "fuel", "transportation", new BigDecimal("1.00"), 1L),
                new Reading("car_1", "fuel", "transportation", new BigDecimal("2.00"), 2L),
                new Reading("car_1", "fuel", "transportation", new BigDecimal("3.00"), 3L)
        );

        when(repository.findReadingsBySensorIdAndTypeAndBetween(query.getSensorId(), query.getType(), query.getFrom(),
                query.getUntil())).thenReturn(readings);

        ReadingStatistics statistics = processor.handle(query);
        assertEquals(new BigDecimal("1.00"), statistics.getMin());
        assertEquals(new BigDecimal("3.00"), statistics.getMax());
        assertEquals(new BigDecimal("2.00"), statistics.getAverage());
        assertEquals(new BigDecimal("2.00"), statistics.getMedian());
    }

    @Test
    public void shouldReturnAppropriateMedian() {
        FetchReadingStatisticsQuery query = new FetchReadingStatisticsQuery(null, "fuel", "transportation", 1L, 10L);
        List<Reading> readings = Arrays.asList(
                new Reading("car_1", "fuel", "transportation", new BigDecimal("1.00"), 1L),
                new Reading("car_1", "fuel", "transportation", new BigDecimal("2.00"), 2L),
                new Reading("car_1", "fuel", "transportation", new BigDecimal("3.00"), 3L),
                new Reading("car_1", "fuel", "transportation", new BigDecimal("4.00"), 4L)
        );

        when(repository.findReadingsByTypeAndCategoryAndBetween(query.getType(), query.getCategory(), query.getFrom(),
                query.getUntil())).thenReturn(readings);

        ReadingStatistics statistics = processor.handle(query);
        assertEquals(new BigDecimal("2.50"), statistics.getMedian());
    }
}
