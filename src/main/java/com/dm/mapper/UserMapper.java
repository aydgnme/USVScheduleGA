package com.dm.mapper;

import com.dm.dto.UserDto;
import com.dm.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getRole(),
                entity.isEnabled()
        );
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setEnabled(dto.isEnabled());
        return user;
    }
}