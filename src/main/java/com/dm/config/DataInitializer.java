package com.dm.config;

import com.dm.data.entity.Role;
import com.dm.data.entity.UserEntity;
import com.dm.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initializes demo users for testing if they don't exist.
 */
@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin if not exists
            if (userRepository.findByEmail("admin@usv.ro") == null) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@usv.ro");
                admin.setPassword(passwordEncoder.encode("Test12."));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
                System.out.println("✅ Created admin user: admin@usv.ro / Test12.");
            }

            // Fix or create secretary
            UserEntity secretary = userRepository.findByEmail("secretary@usv.ro").orElse(null);
            if (secretary == null) {
                secretary = new UserEntity();
                secretary.setEmail("secretary@usv.ro");
                secretary.setPassword(passwordEncoder.encode("Test12."));
                secretary.setRole(Role.SECRETARY);
                secretary.setEnabled(true);
                userRepository.save(secretary);
                System.out.println("✅ Created secretary user: secretary@usv.ro / Test12.");
            } else if (secretary.getRole() != Role.SECRETARY || !secretary.isEnabled()) {
                secretary.setRole(Role.SECRETARY);
                secretary.setEnabled(true);
                secretary.setPassword(passwordEncoder.encode("Test12."));
                userRepository.save(secretary);
                System.out.println("✅ Fixed secretary user: secretary@usv.ro / Test12.");
            }

            // Create teacher if not exists
            if (userRepository.findByEmail("teacher@usv.ro") == null) {
                UserEntity teacher = new UserEntity();
                teacher.setEmail("teacher@usv.ro");
                teacher.setPassword(passwordEncoder.encode("Test12."));
                teacher.setRole(Role.TEACHER);
                teacher.setEnabled(true);
                userRepository.save(teacher);
                System.out.println("✅ Created teacher user: teacher@usv.ro / password");
            }
        };
    }
}
