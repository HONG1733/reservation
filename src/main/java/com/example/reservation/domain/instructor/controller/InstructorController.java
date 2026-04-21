package com.example.reservation.domain.instructor.controller;

import com.example.reservation.domain.instructor.dto.InstructorDetailResponseDto;
import com.example.reservation.domain.instructor.dto.InstructorListResponseDto;
import com.example.reservation.domain.instructor.dto.InstructorRequestDto;
import com.example.reservation.domain.instructor.dto.LoginRequestDto;
import com.example.reservation.domain.instructor.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "강사 등록", description = "관리자만 등록 가능")
    @PostMapping
    public ResponseEntity<String> registerInstructor(@RequestBody InstructorRequestDto dto) {
        instructorService.registerInstructor(dto);
        return ResponseEntity.ok("강사 등록 성공");
    }


    @PostMapping("/login")
    @Operation(summary = "강사 로그인", description = "강사 로그인 가능")
    public ResponseEntity<String> loginInstructor(@RequestBody LoginRequestDto dto) {
        String token = instructorService.login(dto);
        return ResponseEntity.ok(token);
    }

    @GetMapping
    @Operation(summary = "강사 목록 조회", description = "강사 목록 조회 가능")
    public ResponseEntity<List<InstructorListResponseDto>> listResponse() {
        return ResponseEntity.ok(instructorService.getInstructorList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "강사 상세 조회", description = "강사 상세 조회 가능")
    public ResponseEntity<InstructorDetailResponseDto> getInstructorDetail(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.getInstructorDetail(id));
    }



}
