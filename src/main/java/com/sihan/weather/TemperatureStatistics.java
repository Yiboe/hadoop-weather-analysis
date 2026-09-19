package com.sihan.weather;

import java.util.Locale;

final class TemperatureStatistics {
    private int minTenths = Integer.MAX_VALUE;
    private int maxTenths = Integer.MIN_VALUE;
    private long totalTenths;
    private long count;

    void add(int temperatureTenths) {
        minTenths = Math.min(minTenths, temperatureTenths);
        maxTenths = Math.max(maxTenths, temperatureTenths);
        totalTenths += temperatureTenths;
        count++;
    }

    boolean isEmpty() {
        return count == 0;
    }

    double getMinCelsius() {
        ensureNotEmpty();
        return minTenths / 10.0;
    }

    double getMaxCelsius() {
        ensureNotEmpty();
        return maxTenths / 10.0;
    }

    double getAverageCelsius() {
        ensureNotEmpty();
        return totalTenths / (count * 10.0);
    }

    String format() {
        return String.format(
                Locale.ROOT,
                "%.1f\t%.1f\t%.2f",
                getMinCelsius(),
                getMaxCelsius(),
                getAverageCelsius()
        );
    }

    private void ensureNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException("No temperatures have been added");
        }
    }
}
