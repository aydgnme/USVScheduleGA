package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a time slot in the schedule.
 */
@Entity
@Table(name = "timeslots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The day of the week, e.g., "MONDAY".
     */
    @Column(nullable = false, length = 10)
    private String day;

    /**
     * The start time of the slot, e.g., "08:00".
     */
    @Column(nullable = false, length = 5)
    private String startTime;

    /**
     * The end time of the slot, e.g., "10:00".
     */
    @Column(nullable = false, length = 5)
    private String endTime;
}
