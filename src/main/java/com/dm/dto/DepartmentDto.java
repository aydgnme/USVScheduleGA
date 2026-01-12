package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Department DTO.
 */
public class DepartmentDto implements Serializable {
    private Long id;
    private String code;
    private String name;
    private Long facultyId;
    private Set<SpecializationDto> specializations;
    private Set<TeacherDto> teachers;

    public DepartmentDto() {
    }

    public DepartmentDto(Long id, String code, String name, Long facultyId, Set<SpecializationDto> specializations,
            Set<TeacherDto> teachers) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.facultyId = facultyId;
        this.specializations = specializations;
        this.teachers = teachers;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public Set<SpecializationDto> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<SpecializationDto> specializations) {
        this.specializations = specializations;
    }

    public Set<TeacherDto> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<TeacherDto> teachers) {
        this.teachers = teachers;
    }
}
