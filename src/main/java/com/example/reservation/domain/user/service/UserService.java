package com.example.reservation.domain.user.service;

import com.example.reservation.config.JwtTokenProvider;
import com.example.reservation.domain.user.dto.LoginRequestDto;
import com.example.reservation.domain.user.dto.SignupRequestDto;
import com.example.reservation.domain.user.dto.UserUpdateRequestDto;
import com.example.reservation.domain.user.entity.Role;
import com.example.reservation.domain.user.entity.User;
import com.example.reservation.domain.user.repository.UserRepository;
import com.example.reservation.global.exception.AuthenticationException;
import com.example.reservation.global.exception.ResourceNotFoundException;
import com.example.reservation.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public void signup(SignupRequestDto dto) {
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. DB에 저장
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .phone(dto.getPhone())
                .role(Role.MEMBER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    // 로그인
    public String login(LoginRequestDto dto) {
        // 1. 이메일로 회원 조회
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 2. 비밀번호 일치 확인
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 3. JWT 토큰 발급 후 반환
        return jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
    }

    // 회원 정보 수정
    @Transactional
    public void updateUser(Long userId, UserUpdateRequestDto dto, String currentUserEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 회원입니다"));

        // 본인만 수정 가능
        if (!user.getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedException("본인의 정보만 수정할 수 있습니다");
        }

        // 이름 수정
        if (dto.getName() != null) {
            user.updateName(dto.getName());
        }

        // 전화번호 수정
        if (dto.getPhone() != null) {
            user.updatePhone(dto.getPhone());
        }

        // 비밀번호 수정
        if (dto.getPassword() != null) {
            user.updatePassword(passwordEncoder.encode(dto.getPassword()));
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId, String currentUserEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 회원입니다"));

        // 본인만 탈퇴 가능
        if (!user.getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedException("본인의 계정만 탈퇴할 수 있습니다");
        }

        // 예약 내역 확인 (진행 중인 예약이 있으면 탈퇴 불가)
        // TODO: ReservationRepository 완성 후 추가
        // List<Reservation> activeReservations = reservationRepository
        //     .findByUserIdAndStatus(userId, ReservationStatus.CONFIRMED);
        // if (!activeReservations.isEmpty()) {
        //     throw new IllegalStateException("진행 중인 예약이 있어 탈퇴할 수 없습니다");
        // }

        userRepository.delete(user);
    }





}
