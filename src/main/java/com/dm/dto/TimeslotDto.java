package com.dm.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dm.data.entity.Timeslot}
 */
@Value
public class TimeslotDto implements Serializable {
    Long id;
    String day;
    String startTime;
    String endTime;
}