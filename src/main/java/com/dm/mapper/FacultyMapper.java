package com.dm.mapper;

import com.dm.data.entity.FacultyEntity;
import com.dm.dto.FacultyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Faculty entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface FacultyMapper {

    @Mapping(source = "departments", target = "departments", ignore = true)
    FacultyDto toDto(FacultyEntity entity);

    @Mapping(source = "departments", target = "departments", ignore = true)
    FacultyEntity toEntity(FacultyDto dto);
}
