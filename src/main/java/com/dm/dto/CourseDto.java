package com.dm.dto;

import lombok.Value;
import java.io.Serializable;

/**
 * DTO for {@link com.dm.data.entity.Course}
 */
@Value
public class CourseDto implements Serializable {
    Long id;
    String code;
    String title;
    String type;
    int duration;
    String parity;

    Long teacherId;
    String teacherName;

    Long groupId;
    String groupName;
}