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
}
