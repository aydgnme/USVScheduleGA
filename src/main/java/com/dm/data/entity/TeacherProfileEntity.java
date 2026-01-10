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
}
