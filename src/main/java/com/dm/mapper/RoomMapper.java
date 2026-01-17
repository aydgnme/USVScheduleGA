package com.dm.mapper;

import com.dm.data.entity.RoomEntity;
import com.dm.dto.RoomDto;
import com.dm.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for RoomEntity ⇄ RoomDto and RoomEntity ⇄ Room domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    @Mapping(source = "featuresJson", target = "features", ignore = true)
    RoomDto toDto(RoomEntity entity);

    @Mapping(source = "featuresJson", target = "features", ignore = true)
    Room toDomain(RoomEntity entity);

    @Mapping(target = "featuresJson", ignore = true)
    RoomEntity toEntity(RoomDto dto);

    @Mapping(target = "featuresJson", ignore = true)
    RoomEntity toEntityFromDomain(Room domain);
}
