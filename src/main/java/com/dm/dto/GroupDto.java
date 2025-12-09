package com.dm.dto;

import com.dm.model.Group;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Group}
 */
@Value
public class GroupDto implements Serializable {
    Long id;
    String name;
    int studyYear;
    String specialization;
    String faculty;
}