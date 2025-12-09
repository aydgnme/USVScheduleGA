package com.dm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timeslots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String day; // MONDAY, TUESDAY, etc.

    @Column(nullable = false, length = 5)
    private String startTime; // e.g. "08:00"

    @Column(nullable = false, length = 5)
    private String endTime; // e.g. "10:00"
}