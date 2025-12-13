package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a teacher in the application's domain model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    private Long id;
    private String name;
    private String email;
    private int maxHoursWeekly;
    private String department;
    private String availableDays;
    private String preferredTime;
}