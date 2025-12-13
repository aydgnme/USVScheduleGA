package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a room in the application's domain model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    private Long id;
    private String code;
    private int capacity;
    private String type;
}