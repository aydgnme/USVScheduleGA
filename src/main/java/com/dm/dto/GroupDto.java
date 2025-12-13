package com.dm.dto;

import lombok.Value;
import java.io.Serializable;

/**
 * DTO for {@link com.dm.data.entity.Group}
 */
@Value
public class GroupDto implements Serializable {
    Long id;
    String name;
    int studyYear;
    String specialization;
    String faculty;
}
