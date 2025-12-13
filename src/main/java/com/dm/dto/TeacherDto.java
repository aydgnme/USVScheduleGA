package com.dm.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dm.data.entity.Teacher}
 */
@Value
public class TeacherDto implements Serializable {
    Long id;
    String name;
    String email;
    Integer maxHoursWeekly;
    String department;
    String availableDays;
    String preferredTime;
}