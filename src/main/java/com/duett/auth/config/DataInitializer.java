package com.duett.auth.config;

import com.duett.auth.entity.Role;
import com.duett.auth.entity.User;
import com.duett.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail("admin@admin.com")) {
            return;
        }

        User admin = User.builder()
                .name("Admin")
                .email("admin@admin.com")
                .cpf("00000000000")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .tokenVersion(0)
                .build();

        userRepository.save(admin);
    }
}