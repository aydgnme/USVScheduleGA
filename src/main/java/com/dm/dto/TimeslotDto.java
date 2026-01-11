package com.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Timeslot DTO using day and LocalTime fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotDto implements Serializable {
    Long id;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
    Integer durationMinutes;
}