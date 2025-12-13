package com.dm.mapper;

import com.dm.data.entity.Group;
import com.dm.dto.GroupDto;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public GroupDto toDto(Group group) {
        if (group == null) {
            return null;
        }
        return new GroupDto(
                group.getId(),
                group.getName(),
                group.getStudyYear(),
                group.getSpecialization(),
                group.getFaculty()
        );
    }

    public Group toEntity(GroupDto groupDto) {
        if (groupDto == null) {
            return null;
        }
        Group group = new Group();
        group.setId(groupDto.getId());
        group.setName(groupDto.getName());
        group.setStudyYear(groupDto.getStudyYear());
        group.setSpecialization(groupDto.getSpecialization());
        group.setFaculty(groupDto.getFaculty());
        return group;
    }
}
