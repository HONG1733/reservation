package com.example.reservation.domain.user.controller;

import com.example.reservation.domain.user.dto.LoginRequestDto;
import com.example.reservation.domain.user.dto.SignupRequestDto;
import com.example.reservation.domain.user.dto.UserResponseDto;
import com.example.reservation.domain.user.dto.UserUpdateRequestDto;
import com.example.reservation.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 가입 가능")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    @Operation(summary = "회원 로그인", description = "회원 로그인 가능")
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
    @Operation(summary = "마이페이지 조회", description = "회원 본인만 조회 가능")
    public ResponseEntity<UserResponseDto> myPage(
            @AuthenticationPrincipal User user) {

        String email = user.getUsername();

        return ResponseEntity.ok(userService.getMyPage(email));
    }

}
