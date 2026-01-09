package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Specialization DTO.
 */
@Value
public class SpecializationDto implements Serializable {
    Long id;
    String code;
    String name;
    String studyCycle;
    Long departmentId;
    Set<GroupDto> groups;
}
