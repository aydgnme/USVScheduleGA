package com.dm.mapper;

import com.dm.data.entity.DepartmentEntity;
import com.dm.dto.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Department entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    @Mapping(source = "specializations", target = "specializations", ignore = true)
    @Mapping(source = "teachers", target = "teachers", ignore = true)
    DepartmentDto toDto(DepartmentEntity entity);

    @Mapping(source = "facultyId", target = "faculty.id")
    @Mapping(source = "specializations", target = "specializations", ignore = true)
    @Mapping(source = "teachers", target = "teachers", ignore = true)
    DepartmentEntity toEntity(DepartmentDto dto);
}
