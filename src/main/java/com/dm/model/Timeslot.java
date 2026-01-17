package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Domain timeslot with day and time range.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeslot {

    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
}