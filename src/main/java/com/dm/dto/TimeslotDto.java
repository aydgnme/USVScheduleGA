package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Timeslot DTO using day and LocalTime fields.
 */
@Value
public class TimeslotDto implements Serializable {
    Long id;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
    Integer durationMinutes;
}