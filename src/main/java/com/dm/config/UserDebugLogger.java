package com.dm.config;

import com.dm.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lists all users in the database on startup (for debugging).
 */
@Configuration
public class UserDebugLogger {

    @Bean
    CommandLineRunner logUsers(UserRepository userRepository) {
        return args -> {
            System.out.println("\n=== EXISTING USERS IN DATABASE ===");
            userRepository.findAll().forEach(user -> 
                System.out.println("User: " + user.getEmail() + " | Role: " + user.getRole() + " | Enabled: " + user.isEnabled())
            );
            System.out.println("=== END OF USER LIST ===\n");
        };
    }
}
