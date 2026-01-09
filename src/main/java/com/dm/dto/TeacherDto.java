package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Teacher profile DTO (auth data via userId).
 */
@Value
public class TeacherDto implements Serializable {
    Long id;
    Long userId;
    String firstName;
    String lastName;
    String email;
    Integer maxHoursWeekly;
    Set<DepartmentDto> departments;
    String availableDaysJson;
    String preferredTime;
    String note;
}