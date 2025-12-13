package com.dm.mapper;

import com.dm.data.entity.Room;
import com.dm.dto.RoomDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link Room} entity and {@link RoomDto}.
 */
@Component
public class RoomMapper {

    /**
     * Converts a Room entity to a RoomDto.
     * @param entity The Room entity.
     * @return The corresponding RoomDto.
     */
    public RoomDto toDto(Room entity) {
        if (entity == null) {
            return null;
        }
        return new RoomDto(
                entity.getId(),
                entity.getCode(),
                entity.getCapacity(),
                entity.getType()
        );
    }

    /**
     * Converts a RoomDto to a Room entity.
     * @param dto The RoomDto.
     * @return The corresponding Room entity.
     */
    public Room toEntity(RoomDto dto) {
        if (dto == null) {
            return null;
        }
        Room r = new Room();
        r.setId(dto.getId());
        r.setCode(dto.getCode());
        r.setCapacity(dto.getCapacity());
        r.setType(dto.getType());
        return r;
    }
}
