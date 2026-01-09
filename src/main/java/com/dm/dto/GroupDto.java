package com.dm.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * Group DTO aligned to normalized schema.
 */
@Value
public class GroupDto implements Serializable {
    Long id;
    String code;
    Long specializationId;
    Integer studyYear;
    Integer groupNumber;
    String subgroupIndex;
    Integer isModular;
}
