package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Teacher profile in domain layer; auth data stays in User.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private int maxHoursWeekly;
    private Set<Department> departments;
    private String availableDaysJson;
    private String preferredTime;
    private String note;
}