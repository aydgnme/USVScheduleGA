package com.dm.dto;

import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import lombok.Value;

import java.io.Serializable;

/**
 * Payload for creating/updating courses.
 */
@Value
public class CourseRequestDto implements Serializable {
    String code;
    String title;
    CourseComponentType componentType;
    int credits;
    int semester;
    WeekParity parity;
}
