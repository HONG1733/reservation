package com.example.reservation.domain.instructor.controller;

import com.example.reservation.domain.instructor.dto.InstructorRequestDto;
import com.example.reservation.domain.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> registerInstructor(@RequestBody InstructorRequestDto dto) {
        instructorService.registerInstructor(dto);
        return ResponseEntity.ok("강사 등록 성공");
    }

}
