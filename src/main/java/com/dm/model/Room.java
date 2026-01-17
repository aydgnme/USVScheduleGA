package com.dm.model;

import com.dm.model.types.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Domain room definition with optional features.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    private Long id;
    private String code;
    private Integer capacity;
    private RoomType roomType;
    private String building;
    private Integer computers;
    private Set<String> features;
}