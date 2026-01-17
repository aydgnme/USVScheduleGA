package com.dm.model.types;

import java.time.DayOfWeek;
import java.util.Objects;

/**
 * Teacher availability on a specific day with an optional preference weight.
 */
public record AvailabilitySlot(DayOfWeek day, TimeRange timeRange, int weight) {

    public AvailabilitySlot {
        Objects.requireNonNull(day, "day must not be null");
        Objects.requireNonNull(timeRange, "timeRange must not be null");
        if (weight < 1) {
            weight = 1;
        }
    }

    /**
     * Checks overlap on the same day.
     */
    public boolean overlaps(AvailabilitySlot other) {
        Objects.requireNonNull(other, "other must not be null");
        return day.equals(other.day()) && timeRange.overlaps(other.timeRange());
    }
}
