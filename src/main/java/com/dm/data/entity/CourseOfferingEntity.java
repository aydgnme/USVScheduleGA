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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public TeacherProfileEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherProfileEntity teacher) {
        this.teacher = teacher;
    }

    public Integer getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(Integer weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public WeekParity getParity() {
        return parity;
    }

    public void setParity(WeekParity parity) {
        this.parity = parity;
    }

    public com.dm.model.types.ActivityType getType() {
        return type;
    }

    public void setType(com.dm.model.types.ActivityType type) {
        this.type = type;
    }
}
