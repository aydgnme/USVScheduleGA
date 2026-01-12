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
    com.dm.model.types.ActivityType type;

    // Display helpers
    String courseCode;
    String courseTitle;
    String groupCode;
    String teacherFirstName;
    String teacherLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getTeacherFirstName() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public WeekParity getParity() {
        return parity;
    }

    public void setParity(WeekParity parity) {
        this.parity = parity;
    }

    public com.dm.model.types.ActivityType getType() {
        return type;
    }

    public void setType(com.dm.model.types.ActivityType type) {
        this.type = type;
    }
}
