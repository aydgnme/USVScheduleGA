package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Teacher profile separated from authentication user.
 * Linked to User via 1:1 relationship.
 * Can be affiliated with multiple departments.
 */
@Entity
@Table(name = "teacher_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        // Read-only or split logic if needed, but for now irrelevant
    }

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_departments", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
    private java.util.Set<DepartmentEntity> departments = new java.util.HashSet<>();

    @Column(name = "max_hours_weekly")
    private Integer maxHoursWeekly;

    @Lob
    @Column(name = "available_days_json", columnDefinition = "TEXT")
    private String availableDaysJson;

    @Column(name = "preferred_time", length = 100)
    private String preferredTime;

    @Column(name = "note", length = 2000)
    private String note;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public java.util.Set<DepartmentEntity> getDepartments() {
        return departments;
    }

    public void setDepartments(java.util.Set<DepartmentEntity> departments) {
        this.departments = departments;
    }

    public Integer getMaxHoursWeekly() {
        return maxHoursWeekly;
    }

    public void setMaxHoursWeekly(Integer maxHoursWeekly) {
        this.maxHoursWeekly = maxHoursWeekly;
    }

    public String getAvailableDaysJson() {
        return availableDaysJson;
    }

    public void setAvailableDaysJson(String availableDaysJson) {
        this.availableDaysJson = availableDaysJson;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
