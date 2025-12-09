package com.dm.dto;

import com.dm.model.Timeslot;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Timeslot}
 */
@Value
public class TimeslotDto implements Serializable {
    Long id;
    String day;
    String startTime;
    String endTime;
}