package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for a single availability slot with preference weight.
 */
@Value
public class AvailabilitySlotDto implements Serializable {
    DayOfWeek day;
    LocalTime startTime;
    LocalTime endTime;
    int weight;
}
