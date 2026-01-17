package com.dm.model;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Specialization domain model (no persistence annotations).
 */
@Value
public class Specialization implements Serializable {
    Long id;
    String code;
    String name;
    String studyCycle;
    Department department;
    Set<Group> groups;
}
