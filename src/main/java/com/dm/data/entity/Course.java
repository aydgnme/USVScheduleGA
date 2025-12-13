package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a course to be scheduled.
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique code for the course, e.g., "PPAW".
     */
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    /**
     * The full title of the course.
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * The type of the course, e.g., "Lecture", "Lab", "Seminar".
     */
    @Column(nullable = false, length = 20)
    private String type;

    /**
     * The teacher assigned to the course.
     */
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    /**
     * The group of students taking the course.
     */
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    /**
     * The duration of the course in hours.
     */
    @Column(nullable = false)
    private int duration;

    /**
     * The parity of the week for the course, e.g., "Odd", "Even", or "Both".
     */
    @Column(length = 20)
    private String parity;
}
