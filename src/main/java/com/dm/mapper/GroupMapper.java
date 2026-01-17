package com.dm.mapper;

import com.dm.data.entity.GroupEntity;
import com.dm.dto.GroupDto;
import com.dm.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for GroupEntity ⇄ GroupDto and GroupEntity ⇄ Group domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {

    @Mapping(source = "specialization.id", target = "specializationId")
    GroupDto toDto(GroupEntity entity);

    @Mapping(source = "specialization.id", target = "specializationId")
    @Mapping(source = "specialization", target = "specialization", ignore = true)
    Group toDomain(GroupEntity entity);

    @Mapping(source = "specializationId", target = "specialization.id")
    GroupEntity toEntity(GroupDto dto);

    @Mapping(source = "specializationId", target = "specialization.id")
    GroupEntity toEntityFromDomain(Group domain);
}
