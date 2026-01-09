package com.dm.dto;

import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import lombok.Value;

import java.io.Serializable;

/**
 * Course definition DTO (no assignments here).
 */
@Value
public class CourseDto implements Serializable {
    Long id;
    String code;
    String title;
    CourseComponentType componentType;
    int credits;
    int semester;
    WeekParity parity;
}