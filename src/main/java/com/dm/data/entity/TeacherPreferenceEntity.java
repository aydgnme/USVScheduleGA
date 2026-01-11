package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.DayOfWeek;

/**
 * Represents a specific constraint/preference for a teacher.
 * Example: "I want to teach Course X to Group Y on Tuesday".
 * Can be broad ("All Year 2 on Tuesday") or specific.
 */
@Entity
@Table(name = "teacher_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherProfileEntity teacher;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course; // Nullable if preference applies to any course

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group; // Nullable if preference applies to any group

    @Column(name = "study_year")
    private Integer studyYear; // Nullable if preference applies to any year

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "priority")
    private Integer priority; // Higher number = higher priority (e.g., 10=Mandatory, 1=Preferred)
}
