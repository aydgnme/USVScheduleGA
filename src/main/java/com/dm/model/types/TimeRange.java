package com.dm.model.types;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Immutable time range within a day.
 */
public record TimeRange(LocalTime start, LocalTime end) {

    public TimeRange {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("end must be after start");
        }
    }

    /**
     * Returns true when the ranges overlap on the same day.
     */
    public boolean overlaps(TimeRange other) {
        Objects.requireNonNull(other, "other must not be null");
        return start.isBefore(other.end) && other.start().isBefore(end);
    }

    /**
     * Duration in minutes.
     */
    public long durationMinutes() {
        return ChronoUnit.MINUTES.between(start, end);
    }
}
