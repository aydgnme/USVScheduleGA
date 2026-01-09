package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Department DTO.
 */
@Value
public class DepartmentDto implements Serializable {
    Long id;
    String code;
    String name;
    Long facultyId;
    Set<SpecializationDto> specializations;
    Set<TeacherDto> teachers;
}
