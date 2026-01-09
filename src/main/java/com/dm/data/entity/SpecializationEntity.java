package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Specialization Entity
 * Represents study programs within departments
 * E.g., AIA (Bachelor), CSEE (Master), PIPP-Conv (Conversion)
 */
@Entity
@Table(name = "specializations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "study_cycle", nullable = false, length = 20)
    private String studyCycle; // BACHELOR, MASTER, DOCTORATE, CONVERSION

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<GroupEntity> groups = new java.util.HashSet<>();
}
