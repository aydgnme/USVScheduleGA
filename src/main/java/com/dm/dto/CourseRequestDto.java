package com.dm.dto;

import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import lombok.Value;

import java.io.Serializable;

/**
 * Payload for creating/updating courses.
 */
public class CourseRequestDto implements Serializable {
    private String code;
    private String title;
    private CourseComponentType componentType;
    private int credits;
    private int semester;
    private WeekParity parity;

    public CourseRequestDto() {
    }

    public CourseRequestDto(String code, String title, CourseComponentType componentType, int credits, int semester,
            WeekParity parity) {
        this.code = code;
        this.title = title;
        this.componentType = componentType;
        this.credits = credits;
        this.semester = semester;
        this.parity = parity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CourseComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(CourseComponentType componentType) {
        this.componentType = componentType;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public WeekParity getParity() {
        return parity;
    }

    public void setParity(WeekParity parity) {
        this.parity = parity;
    }
}
