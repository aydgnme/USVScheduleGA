package com.dm.mapper;

import com.dm.data.entity.UserEntity;
import com.dm.dto.UserDto;
import com.dm.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for UserEntity ⇄ UserDto and UserEntity ⇄ User domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(UserEntity entity);

    User toDomain(UserEntity entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "teacherProfile", ignore = true)
    UserEntity toEntity(UserDto dto);

    @Mapping(target = "teacherProfile", ignore = true)
    UserEntity toEntityFromDomain(User domain);
}