package com.dm.mapper;

import com.dm.dto.TeacherDto;
import com.dm.data.entity.Teacher;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link Teacher} entity and {@link TeacherDto}.
 */
@Component
public class TeacherMapper {

    /**
     * Converts a Teacher entity to a TeacherDto.
     * @param entity The Teacher entity.
     * @return The corresponding TeacherDto.
     */
    public TeacherDto toDto(Teacher entity) {
        if (entity == null) return null;

        return new TeacherDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getMaxHoursWeekly(),
                entity.getDepartment(),
                entity.getAvailableDays(),
                entity.getPreferredTime()
        );
    }

    /**
     * Converts a TeacherDto to a Teacher entity.
     * @param dto The TeacherDto.
     * @return The corresponding Teacher entity.
     */
    public Teacher toEntity(TeacherDto dto) {
        if (dto == null) return null;

        Teacher t = new Teacher();
        t.setId(dto.getId());
        t.setName(dto.getName());
        t.setEmail(dto.getEmail());
        t.setMaxHoursWeekly(
                dto.getMaxHoursWeekly() != null ? dto.getMaxHoursWeekly() : 0
        );
        t.setDepartment(dto.getDepartment());
        t.setAvailableDays(dto.getAvailableDays());
        t.setPreferredTime(dto.getPreferredTime());
        return t;
    }
}