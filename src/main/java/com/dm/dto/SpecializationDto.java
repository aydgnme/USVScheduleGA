package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Specialization DTO.
 */
public class SpecializationDto implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String studyCycle;
    private Long departmentId;
    private Set<GroupDto> groups;

    public SpecializationDto() {
    }

    public SpecializationDto(Long id, String code, String name, String studyCycle, Long departmentId,
            Set<GroupDto> groups) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.studyCycle = studyCycle;
        this.departmentId = departmentId;
        this.groups = groups;
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

    public String getStudyCycle() {
        return studyCycle;
    }

    public void setStudyCycle(String studyCycle) {
        this.studyCycle = studyCycle;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Set<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDto> groups) {
        this.groups = groups;
    }
}
