package com.dm.dto;

import com.dm.model.types.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Room DTO with type, building, and feature list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto implements Serializable {
    Long id;
    String code;
    Integer capacity;
    RoomType roomType;
    String building;
    Integer computers;
    Set<String> features;
}