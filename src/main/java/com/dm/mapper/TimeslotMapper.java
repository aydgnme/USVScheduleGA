package com.dm.mapper;

import com.dm.data.entity.TimeslotEntity;
import com.dm.dto.TimeslotDto;
import com.dm.model.Timeslot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper for TimeslotEntity ⇄ TimeslotDto and TimeslotEntity ⇄ Timeslot domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimeslotMapper {

    TimeslotDto toDto(TimeslotEntity entity);

    Timeslot toDomain(TimeslotEntity entity);

    TimeslotEntity toEntity(TimeslotDto dto);

    TimeslotEntity toEntityFromDomain(Timeslot domain);
}