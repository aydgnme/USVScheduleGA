package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Department Entity
 * Represents departments within faculties
 * E.g., Calculatoare, Electrotehnică, etc.
 */
@Entity
@Table(name = "departments", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "faculty_id", "code" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyEntity faculty;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<SpecializationEntity> specializations = new java.util.HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    private java.util.Set<TeacherProfileEntity> teachers = new java.util.HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacultyEntity getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyEntity faculty) {
        this.faculty = faculty;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Set<SpecializationEntity> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(java.util.Set<SpecializationEntity> specializations) {
        this.specializations = specializations;
    }

    public java.util.Set<TeacherProfileEntity> getTeachers() {
        return teachers;
    }

    public void setTeachers(java.util.Set<TeacherProfileEntity> teachers) {
        this.teachers = teachers;
    }
}
