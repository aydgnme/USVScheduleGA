package com.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Group DTO aligned to normalized schema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto implements Serializable {
    Long id;
    String code;
    Long specializationId;
    Integer studyYear;
    Integer groupNumber;
    String subgroupIndex;
    Integer isModular;

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

    public Long getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Long specializationId) {
        this.specializationId = specializationId;
    }

    public Integer getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSubgroupIndex() {
        return subgroupIndex;
    }

    public void setSubgroupIndex(String subgroupIndex) {
        this.subgroupIndex = subgroupIndex;
    }

    public Integer getIsModular() {
        return isModular;
    }

    public void setIsModular(Integer isModular) {
        this.isModular = isModular;
    }
}
