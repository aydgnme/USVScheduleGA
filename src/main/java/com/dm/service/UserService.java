package com.dm.service;

import com.dm.data.repository.UserRepository;
import com.dm.dto.UserDto;
import com.dm.mapper.UserMapper;
import com.dm.model.Role;
import com.dm.model.User;
import org.springframework.security.core.userdetails.*;
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

    // Spring Security entrypoint
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found: " + email);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }

    // Normal CRUD operations for UI/API

    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getPendingApprovals() {
        return repository.findAll().stream()
                .filter(u -> !u.isEnabled())
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto registerTeacher(String email, String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.TEACHER);
        user.setEnabled(false); // bekliyor
        return mapper.toDto(repository.save(user));
    }

    public UserDto registerSecretary(String email, String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.SECRETARY);
        user.setEnabled(true);
        return mapper.toDto(repository.save(user));
    }

    public void approveUser(Long id) {
        User user = repository.findById(id).orElseThrow();
        user.setEnabled(true);
        repository.save(user);
    }
}