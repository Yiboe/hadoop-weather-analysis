package com.sihan.weather;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemperatureStatisticsTest {
    @Test
    void calculatesNegativeTemperatureStatisticsFromTenths() {
        TemperatureStatistics statistics = new TemperatureStatistics();
        statistics.add(-227);
        statistics.add(-215);

        assertEquals(-22.7, statistics.getMinCelsius(), 0.0001);
        assertEquals(-21.5, statistics.getMaxCelsius(), 0.0001);
        assertEquals(-22.1, statistics.getAverageCelsius(), 0.0001);
        assertEquals("-22.7\t-21.5\t-22.10", statistics.format());
    }

    @Test
    void rejectsReadingAnEmptyAggregation() {
        TemperatureStatistics statistics = new TemperatureStatistics();
        assertThrows(IllegalStateException.class, statistics::getAverageCelsius);
    }
}
