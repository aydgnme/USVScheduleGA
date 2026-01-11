package com.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Teacher profile DTO (auth data via userId).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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