package com.dm.dto;

import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for scheduled entries combining offering, timeslot, and room.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryDto implements Serializable {
    Long id;
    Long offeringId;
    Long timeslotId;
    Long roomId;
    WeekParity weekPattern;
    ScheduleStatus status;

    // View fields
    String courseCode;
    String courseTitle;
    String groupCode;
    String teacherFirstName;
    String teacherLastName;
    String roomCode;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
}
