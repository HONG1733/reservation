package com.example.reservation.domain.instructor.service;

import com.example.reservation.config.JwtTokenProvider;
import com.example.reservation.domain.instructor.dto.InstructorRequestDto;
import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerInstructor(InstructorRequestDto dto) {
        // 1. 이메일 중복 체크
        if (instructorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. DB에 저장
        Instructor instructor = Instructor.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .phone(dto.getPhone())
                .profileImageUrl(dto.getProfileImageUrl())
                .speciality(dto.getSpeciality())
                .career(dto.getCareer())
                .certificate(dto.getCertificate())
                .introduction(dto.getIntroduction())
                .createdAt(LocalDateTime.now())
                .build();

        instructorRepository.save(instructor);
    }
}
