package com.dm.mapper;

import com.dm.data.entity.SpecializationEntity;
import com.dm.dto.SpecializationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Specialization entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SpecializationMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "groups", target = "groups", ignore = true)
    SpecializationDto toDto(SpecializationEntity entity);

    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(source = "groups", target = "groups", ignore = true)
    SpecializationEntity toEntity(SpecializationDto dto);
}
