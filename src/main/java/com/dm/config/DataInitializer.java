package com.dm.config;

import com.dm.data.entity.Role;
import com.dm.data.entity.User;
import com.dm.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initializes demo users for testing if they don't exist.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin if not exists
            if (userRepository.findByEmail("admin@usv.ro") == null) {
                User admin = new User();
                admin.setEmail("admin@usv.ro");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
                System.out.println("✅ Created admin user: admin@usv.ro / password");
            }

            // Fix or create secretary
            User secretary = userRepository.findByEmail("secretary@usv.ro");
            if (secretary == null) {
                secretary = new User();
                secretary.setEmail("secretary@usv.ro");
                secretary.setPassword(passwordEncoder.encode("password"));
                secretary.setRole(Role.SECRETARY);
                secretary.setEnabled(true);
                userRepository.save(secretary);
                System.out.println("✅ Created secretary user: secretary@usv.ro / password");
            } else if (secretary.getRole() != Role.SECRETARY || !secretary.isEnabled()) {
                secretary.setRole(Role.SECRETARY);
                secretary.setEnabled(true);
                secretary.setPassword(passwordEncoder.encode("password"));
                userRepository.save(secretary);
                System.out.println("✅ Fixed secretary user: secretary@usv.ro / password");
            }

            // Create teacher if not exists
            if (userRepository.findByEmail("teacher@usv.ro") == null) {
                User teacher = new User();
                teacher.setEmail("teacher@usv.ro");
                teacher.setPassword(passwordEncoder.encode("password"));
                teacher.setRole(Role.TEACHER);
                teacher.setEnabled(true);
                userRepository.save(teacher);
                System.out.println("✅ Created teacher user: teacher@usv.ro / password");
            }
        };
    }
}
