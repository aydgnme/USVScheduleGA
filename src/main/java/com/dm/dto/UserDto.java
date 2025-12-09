package com.dm.dto;

import com.dm.model.Role;
import java.io.Serializable;

/**
 * DTO for {@link com.dm.model.User}
 */
public class UserDto implements Serializable {
    private Long id;
    private String email;
    private Role role;
    private boolean enabled;

    public UserDto() {}

    public UserDto(Long id, String email, Role role, boolean enabled) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}