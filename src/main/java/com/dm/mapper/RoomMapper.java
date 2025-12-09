package com.dm.mapper;

import com.dm.dto.RoomDto;
import com.dm.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDto toDto(Room entity) {
        return new RoomDto(
                entity.getId(),
                entity.getCode(),
                entity.getCapacity(),
                entity.getType()
        );
    }

    public Room toEntity(RoomDto dto) {
        Room r = new Room();
        r.setId(dto.getId());
        r.setCode(dto.getCode());
        r.setCapacity(dto.getCapacity());
        r.setType(dto.getType());
        return r;
    }
}