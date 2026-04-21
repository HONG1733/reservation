package com.example.reservation.domain.instructor.service;

import com.example.reservation.config.JwtTokenProvider;
import com.example.reservation.domain.instructor.dto.InstructorDetailResponseDto;
import com.example.reservation.domain.instructor.dto.InstructorListResponseDto;
import com.example.reservation.domain.instructor.dto.InstructorRequestDto;
import com.example.reservation.domain.instructor.dto.LoginRequestDto;
import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 강사 등록
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

    // 강사 로그인
    public String login(LoginRequestDto dto) {

        // 이메일 조회
        Instructor instructor = instructorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(dto.getPassword(), instructor.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다");
        }
        // jwt 토큰 발급 후 반환
        return jwtTokenProvider.generateToken(instructor.getEmail(), "INSTRUCTOR");
    }

    // 강사 목록 조회
    @Transactional(readOnly = true)
    public List<InstructorListResponseDto> getInstructorList() {

        List<Instructor> instructors = instructorRepository.findAll();

        return instructors.stream()
                .map(instructor -> new InstructorListResponseDto(
                        instructor.getId(),
                        instructor.getName()
                ))
                .toList();
    }

    // 강사 상세 조회
    public InstructorDetailResponseDto getInstructorDetail(Long instructorId) {
        // 강사 조회
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("강사를 찾을 수 없습니다."));
        // 강사 상세 반환
        return InstructorDetailResponseDto.from(instructor);

    }


    // 강사 정보 수정
    // 일단 보류

    // 강사 탈퇴
    // 일단 보류

}
