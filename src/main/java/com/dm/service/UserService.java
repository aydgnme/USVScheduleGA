package com.dm.service;

import com.dm.data.entity.Role;
import com.dm.data.entity.UserEntity;
import com.dm.data.repository.UserRepository;
import com.dm.dto.UserDto;
import com.dm.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Spring Security entrypoint — load user for authentication
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }

    /**
     * Returns all users as DTO list
     */
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Returns users waiting for secretary approval
     */
    public List<UserDto> getPendingApprovals() {
        return repository.findAll().stream()
                .filter(u -> !u.isEnabled())
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Registers a new teacher (inactive until approved)
     */
    public UserDto registerTeacher(String email, String rawPassword) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.TEACHER);
        user.setEnabled(false); // waiting approval
        return mapper.toDto(repository.save(user));
    }

    /**
     * Registers a secretary (auto-approved)
     */
    public UserDto registerSecretary(String email, String rawPassword) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.SECRETARY);
        user.setEnabled(true);
        return mapper.toDto(repository.save(user));
    }

    /**
     * Approves a pending teacher (activated by secretary)
     */
    public void approveUser(Long id) {
        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        user.setEnabled(true);
        repository.save(user);
    }
}