package com.dm.dto;

import com.dm.model.types.RoomType;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * Room DTO with type, building, and feature list.
 */
@Value
public class RoomDto implements Serializable {
    Long id;
    String code;
    Integer capacity;
    RoomType roomType;
    String building;
    Integer computers;
    Set<String> features;
}