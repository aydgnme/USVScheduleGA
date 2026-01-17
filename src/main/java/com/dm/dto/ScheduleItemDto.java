package com.dm.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * Deprecated: replaced by ScheduleEntryDto.
 */
@Deprecated
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
