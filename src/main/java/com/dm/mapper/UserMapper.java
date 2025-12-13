package com.dm.mapper;

import com.dm.dto.UserDto;
import com.dm.data.entity.User;
import com.dm.data.entity.Role;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link User} entity and {@link UserDto}.
 */
@Component
public class UserMapper {

    /**
     * Converts a User entity to a UserDto.
     * @param entity The User entity.
     * @return The corresponding UserDto.
     */
    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getRole(),
                entity.isEnabled()
        );
    }

    /**
     * Converts a UserDto to a User entity.
     * @param dto The UserDto.
     * @return The corresponding User entity.
     */
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setEnabled(dto.isEnabled());
        return user;
    }
}