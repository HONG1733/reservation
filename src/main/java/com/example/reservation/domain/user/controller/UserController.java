package com.example.reservation.domain.user.controller;

import com.example.reservation.domain.user.dto.LoginRequestDto;
import com.example.reservation.domain.user.dto.SignupRequestDto;
import com.example.reservation.domain.user.dto.UserUpdateRequestDto;
import com.example.reservation.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "회원 정보 수정", description = "회원 본인만 수정 가능")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId, //URL 경로값을 가져오는 어노테이션
            @Valid @RequestBody UserUpdateRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) { //로그인한 정보를 변수에 담음

        userService.updateUser(userId, dto, userDetails.getUsername());
        return ResponseEntity.ok("회원 정보가 수정되었습니다");
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "회원 탈퇴", description = "회원 본인만 탈퇴 가능")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        userService.deleteUser(userId, userDetails.getUsername());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다");
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회 (디버깅용)")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok("userDetails is null - 인증 실패!");
        }
        return ResponseEntity.ok("로그인 이메일: " + userDetails.getUsername());
    }

}
