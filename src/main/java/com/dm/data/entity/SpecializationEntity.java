package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "study_cycle", nullable = false, length = 20)
    private String studyCycle; // BACHELOR, MASTER, DOCTORATE, CONVERSION

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<GroupEntity> groups = new java.util.HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
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

    public String getStudyCycle() {
        return studyCycle;
    }

    public void setStudyCycle(String studyCycle) {
        this.studyCycle = studyCycle;
    }

    public java.util.Set<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(java.util.Set<GroupEntity> groups) {
        this.groups = groups;
    }
}
