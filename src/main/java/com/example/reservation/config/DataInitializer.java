package com.example.reservation.config;

import com.example.reservation.domain.user.entity.Role;
import com.example.reservation.domain.user.entity.User;
import com.example.reservation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = User.builder()
                    .name("관리자")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .phone("010-0000-0000")
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
        }
    }
}
