package com.dm.mapper;

import com.dm.dto.CourseDto;
import com.dm.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDto toDto(Course entity) {
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

    public Course toEntity(CourseDto dto) {
        Course c = new Course();
        c.setId(dto.getId());
        c.setCode(dto.getCode());
        c.setTitle(dto.getTitle());
        c.setType(dto.getType());
        c.setDuration(dto.getDuration());
        c.setParity(dto.getParity());
        return c;
    }
}