package com.dm.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing a teacher's availability.
 */
@Value
public class TeacherAvailabilityDto implements Serializable {
    Long teacherId;
    List<AvailabilitySlotDto> slots;
}
