package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a room where courses can take place.
 */
@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The code for the room, e.g., "C203". Must be unique.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * The maximum capacity of the room.
     */
    @Column(nullable = false)
    private int capacity;

    /**
     * The type of the room, e.g., "Lecture", "Lab".
     */
    @Column(length = 50)
    private String type;
}
