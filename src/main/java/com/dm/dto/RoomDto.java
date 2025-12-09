package com.dm.dto;

import com.dm.model.Room;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Room}
 */
@Value
public class RoomDto implements Serializable {
    Long id;
    String code;
    int capacity;
    String type;
}