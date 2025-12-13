package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a timeslot in the application's domain model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeslot {

    private Long id;
    private String day;
    private String startTime;
    private String endTime;
}