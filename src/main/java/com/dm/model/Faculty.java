package com.dm.model;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Faculty domain model (no persistence annotations).
 */
@Value
public class Faculty implements Serializable {
    Long id;
    String code;
    String name;
    String shortName;
    Set<Department> departments;
}
