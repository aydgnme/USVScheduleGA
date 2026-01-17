package com.dm.model;

import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scheduled placement of a course offering into a timeslot and room.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntry {

    private Long id;
    private CourseOffering offering;
    private Timeslot timeslot;
    private Room room;
    private WeekParity weekPattern;
    private ScheduleStatus status;
}
