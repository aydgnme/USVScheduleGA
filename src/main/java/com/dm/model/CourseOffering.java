package com.dm.model;

import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Course assignment to a group and teacher with weekly load.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOffering {

    private Long id;
    private Course course;
    private Group group;
    private Teacher teacher;
    private int weeklyHours;
    private WeekParity parity;
}
