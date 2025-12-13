package com.dm.data.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user of the application.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's email address. Used for login and must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The user's password. It should be stored in a hashed format.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The role of the user, which determines their permissions.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Flag to indicate if the user account is enabled.
     * A secretary must approve the account before the user can log in.
     */
    @Column(nullable = false)
    private boolean enabled = false;
}
