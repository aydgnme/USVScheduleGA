package com.dm.model;

import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain course definition; assignment to groups/teachers occurs via CourseOffering.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    private Long id;
    private String code;
    private String title;
    private CourseComponentType componentType;
    private int credits;
    private int semester;
    private WeekParity parity;
}
