package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Faculty Entity
 * Represents top-level organizational units in USV
 * E.g., FIESC, FIM, FEFS, SEAB, etc.
 */
@Entity
@Table(name = "faculties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "short_name", length = 100)
    private String shortName;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<DepartmentEntity> departments = new java.util.HashSet<>();
}
