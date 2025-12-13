package com.dm.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dm.data.entity.Room}
 */
@Value
public class RoomDto implements Serializable {
    Long id;
    String code;
    int capacity;
    String type;
}