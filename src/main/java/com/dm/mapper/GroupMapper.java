package com.dm.mapper;

import com.dm.dto.GroupDto;
import com.dm.model.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public GroupDto toDto(Group entity) {
        return new GroupDto(
                entity.getId(),
                entity.getName(),
                entity.getStudyYear(),
                entity.getSpecialization(),
                entity.getFaculty()
        );
    }

    public Group toEntity(GroupDto dto) {
        Group g = new Group();
        g.setId(dto.getId());
        g.setName(dto.getName());
        g.setStudyYear(dto.getStudyYear());
        g.setSpecialization(dto.getSpecialization());
        g.setFaculty(dto.getFaculty());
        return g;
    }
}