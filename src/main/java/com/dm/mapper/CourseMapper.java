package com.dm.mapper;

import com.dm.dto.CourseDto;
import com.dm.data.entity.Course;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link Course} entity and {@link CourseDto}.
 */
@Component
public class CourseMapper {

    /**
     * Converts a Course entity to a CourseDto.
     * @param entity The Course entity.
     * @return The corresponding CourseDto.
     */
    public CourseDto toDto(Course entity) {
        if (entity == null) {
            return null;
        }
        return new CourseDto(
                entity.getId(),
                entity.getCode(),
                entity.getTitle(),
                entity.getType(),
                entity.getDuration(),
                entity.getParity(),
                entity.getTeacher() != null ? entity.getTeacher().getId() : null,
                entity.getTeacher() != null ? entity.getTeacher().getName() : null,
                entity.getGroup() != null ? entity.getGroup().getId() : null,
                entity.getGroup() != null ? entity.getGroup().getName() : null
        );
    }

    /**
     * Converts a CourseDto to a Course entity.
     * Note: This method does not map associated Teacher or Group entities,
     * as they are typically fetched separately or managed by the persistence context.
     * @param dto The CourseDto.
     * @return The corresponding Course entity.
     */
    public Course toEntity(CourseDto dto) {
        if (dto == null) {
            return null;
        }
        Course c = new Course();
        c.setId(dto.getId());
        c.setCode(dto.getCode());
        c.setTitle(dto.getTitle());
        c.setType(dto.getType());
        c.setDuration(dto.getDuration());
        c.setParity(dto.getParity());
        // Teacher and Group relationships are not mapped here,
        // as the DTO only carries IDs/names and not full entity objects.
        // These relationships would typically be set by a service layer.
        return c;
    }
}