package com.dm.mapper;

import com.dm.data.entity.TeacherProfileEntity;
import com.dm.dto.TeacherDto;
import com.dm.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

/**
 * Mapper for TeacherProfileEntity ⇄ TeacherDto and TeacherProfileEntity ⇄
 * Teacher domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { DepartmentMapper.class })
public interface TeacherMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    TeacherDto toDto(TeacherProfileEntity entity);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "departments", target = "departments", ignore = true)
    Teacher toDomain(TeacherProfileEntity entity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "departments", target = "departments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    TeacherProfileEntity toEntity(TeacherDto dto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    TeacherProfileEntity toEntityFromDomain(Teacher domain);
}