package com.dm.mapper;

import com.dm.data.entity.CourseEntity;
import com.dm.dto.CourseDto;
import com.dm.dto.CourseRequestDto;
import com.dm.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for CourseEntity ⇄ CourseDto and CourseEntity ⇄ Course domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    CourseDto toDto(CourseEntity entity);

    Course toDomain(CourseEntity entity);

    CourseEntity toEntity(CourseDto dto);

    CourseEntity toEntityFromDomain(Course domain);

    @Mapping(target = "id", ignore = true)
    CourseEntity toEntityFromRequest(CourseRequestDto request);
}