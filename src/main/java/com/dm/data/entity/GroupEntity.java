package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groups", indexes = {
        @Index(name = "uk_groups_code", columnList = "code", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization", nullable = false)
    private SpecializationEntity specialization;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code; // e.g., AIA-001, C-002

    @Column(name = "study_year", nullable = false)
    private Integer studyYear; // 0-4

    @Column(name = "group_number")
    private Integer groupNumber;

    @Column(name = "subgroup_index", length = 1)
    private String subgroupIndex; // a, b, c, or null

    @Column(name = "is_modular")
    private Integer isModular; // 0 or 1
}
