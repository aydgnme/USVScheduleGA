package com.dm.data.entity;

import com.dm.model.types.WeekParity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_offerings", indexes = {
        @Index(name = "ix_co_course", columnList = "course_id"),
        @Index(name = "ix_co_group", columnList = "group_id"),
        @Index(name = "ix_co_teacher", columnList = "teacher_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOfferingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherProfileEntity teacher;

    @Column(name = "weekly_hours")
    private Integer weeklyHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "parity", nullable = false, length = 10)
    private WeekParity parity;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", length = 20)
    private com.dm.model.types.ActivityType type;
}
