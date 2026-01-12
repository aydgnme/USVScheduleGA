package com.dm.data.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;

/**
 * Represents a specific constraint/preference for a teacher.
 * Example: "I want to teach Course X to Group Y on Tuesday".
 * Can be broad ("All Year 2 on Tuesday") or specific.
 */
@Entity
@Table(name = "teacher_preferences")
public class TeacherPreferenceEntity {

    public TeacherPreferenceEntity() {
    }

    public TeacherPreferenceEntity(Long id, TeacherProfileEntity teacher, CourseEntity course, GroupEntity group,
            Integer studyYear, DayOfWeek dayOfWeek, Integer priority, Integer startHour, Integer endHour,
            com.dm.model.types.PreferenceType type) {
        this.id = id;
        this.teacher = teacher;
        this.course = course;
        this.group = group;
        this.studyYear = studyYear;
        this.dayOfWeek = dayOfWeek;
        this.priority = priority;
        this.startHour = startHour;
        this.endHour = endHour;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TeacherPreferenceEntity{" +
                "id=" + id +
                ", studyYear=" + studyYear +
                ", dayOfWeek=" + dayOfWeek +
                ", priority=" + priority +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", type=" + type +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherProfileEntity teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course; // Nullable if preference applies to any course

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

    @Column(name = "start_hour")
    private Integer startHour;

    @Column(name = "end_hour")
    private Integer endHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type", length = 20)
    private com.dm.model.types.PreferenceType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeacherProfileEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherProfileEntity teacher) {
        this.teacher = teacher;
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

    public Integer getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public com.dm.model.types.PreferenceType getType() {
        return type;
    }

    public void setType(com.dm.model.types.PreferenceType type) {
        this.type = type;
    }
}
