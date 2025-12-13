package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a course in the application's domain model.
 * This is a plain Java object used for business logic and data transfer,
 * without any persistence-specific annotations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    private Long id;
    private String code;
    private String title;
    private String type;
    private Teacher teacher;
    private Group group;
    private int duration;
    private String parity;
}
