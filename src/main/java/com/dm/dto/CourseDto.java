package com.dm.dto;

import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Course definition DTO (no assignments here).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto implements Serializable {
    Long id;
    String code;
    String title;
    CourseComponentType componentType;
    int credits;
    int semester;
    WeekParity parity;
    Long departmentId;
    String departmentName;
}