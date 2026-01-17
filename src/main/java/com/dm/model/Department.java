package com.dm.model;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Department domain model (no persistence annotations).
 */
@Value
public class Department implements Serializable {
    Long id;
    String code;
    String name;
    Faculty faculty;
    Set<Specialization> specializations;
    Set<Teacher> teachers;
}
