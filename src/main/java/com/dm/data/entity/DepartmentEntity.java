package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyEntity faculty;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<SpecializationEntity> specializations = new java.util.HashSet<>();

    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    private java.util.Set<TeacherProfileEntity> teachers = new java.util.HashSet<>();
}
