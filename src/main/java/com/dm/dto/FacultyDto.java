package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Faculty DTO.
 */
@Value
public class FacultyDto implements Serializable {
    Long id;
    String code;
    String name;
    String shortName;
    Set<DepartmentDto> departments;
}
