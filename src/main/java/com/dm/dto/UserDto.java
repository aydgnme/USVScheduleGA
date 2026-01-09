package com.dm.dto;

import com.dm.data.entity.Role;
import lombok.Value;

import java.io.Serializable;

/**
 * User DTO for auth-facing data.
 */
@Value
public class UserDto implements Serializable {
    Long id;
    String email;
    Role role;
    boolean enabled;
}