package com.dm.dto;

import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for course offerings (assignment of a course to a group and teacher).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOfferingDto implements Serializable {
    Long id;
    Long courseId;
    Long groupId;
    Long teacherId;
    int weeklyHours;
    WeekParity parity;

    // Display helpers
    String courseCode;
    String courseTitle;
    String groupCode;
    String teacherFirstName;
    String teacherLastName;
}
