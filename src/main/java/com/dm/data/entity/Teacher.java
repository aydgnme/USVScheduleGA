package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a teacher in the system.
 */
@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full name of the teacher.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The email of the teacher. Must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The maximum number of hours the teacher can work per week.
     */
    @Column(name = "max_hours_weekly", nullable = false)
    private int maxHoursWeekly;

    /**
     * The department the teacher belongs to.
     */
    private String department;

    /**
     * Comma-separated list of days the teacher is available.
     */
    private String availableDays;

    /**
     * Teacher's preferred time slots for teaching.
     */
    private String preferredTime;
}
