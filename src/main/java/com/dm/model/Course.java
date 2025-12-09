package com.dm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code; // e.g. PPAW, AAPTM, CABD

    @Column(nullable = false, length = 100)
    private String title; // e.g. ALGORITMI AVANSAȚI, PROBABILISTICI ȘI TEHNICI METAEURISTICE

    @Column(nullable = false, length = 20)
    private String type; // Lecture, Lab, Seminar

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private int duration;

    @Column(length = 20)
    private String parity; // e.g. "Odd", "Even", "Both"
}
