package com.dm.config;

import com.dm.service.UserService;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private UserService userService;

    @Autowired
    @Lazy
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow PWA/static assets without authentication
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/sw.js",
                        "/sw-runtime-resources-precache.js",
                        "/VAADIN/**",
                        "/frontend/**",
                        "/images/**",
                        "/icons/**",
                        "/manifest.webmanifest",
                        "/offline.html"
                ).permitAll()
        );

        // Register custom auth provider (UserService + encoder)
        http.authenticationProvider(authenticationProvider());

        http.formLogin(form -> form
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login") // Vaadin form POSTs here
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .successHandler((request, response, authentication) -> {
                    var auth = authentication.getAuthorities().iterator().next().getAuthority();
                    if (auth.equals("ROLE_SECRETARY")) {
                        response.sendRedirect("/secretary");
                    } else if (auth.equals("ROLE_TEACHER")) {
                        response.sendRedirect("/teacher");
                    } else if (auth.equals("ROLE_ADMIN")) {
                        response.sendRedirect("/admin");
                    } else {
                        response.sendRedirect("/dashboard"); // Fallback for other roles or general dashboard
                    }
                })
        );

        http.logout(logout -> logout
            // Allow GET anchors from Vaadin and ensure redirect back to login
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login")
                .permitAll()
        );

        // Apply Vaadin defaults (adds its own anyRequest) after custom matchers.
        setLoginView(http, com.dm.view.login.LoginView.class);
        super.configure(http);
    }
}
