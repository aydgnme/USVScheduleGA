package com.dm.dto;

import lombok.Value;
import java.io.Serializable;

/**
 * DTO for schedule items representing a course assignment in a specific timeslot and room.
 */
@Value
public class ScheduleItemDto implements Serializable {
    Long id;
    String day;
    String startTime;
    String endTime;
    String courseCode;
    String courseTitle;
    String roomName;
    String teacherName;
    String groupName;
}
