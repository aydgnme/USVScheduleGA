package com.dm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String name; // e.g. "313A"

    @Column(name = "study_year", nullable = false)
    private int studyYear; // e.g. 3

    @Column(nullable = false, length = 100)
    private String specialization; // e.g. "Calculatoare"

    @Column(length = 10)
    private String faculty; // e.g. "FSE", "FIESC"
}