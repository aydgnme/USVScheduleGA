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
public class CourseDto implements Serializable {
    public CourseDto() {
    }

    public CourseDto(Long id, String code, String title, CourseComponentType componentType, int credits, int semester,
            WeekParity parity, Long departmentId, String departmentName) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.componentType = componentType;
        this.credits = credits;
        this.semester = semester;
        this.parity = parity;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    Long id;
    String code;
    String title;
    CourseComponentType componentType;
    int credits;
    int semester;
    WeekParity parity;
    Long departmentId;
    String departmentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}