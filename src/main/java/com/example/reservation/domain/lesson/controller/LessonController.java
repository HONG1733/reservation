package com.example.reservation.domain.lesson.controller;

import com.example.reservation.domain.lesson.dto.LessonRequestDto;
import com.example.reservation.domain.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;



    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody LessonRequestDto dto) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        lessonService.createLesson(email, dto);
        return ResponseEntity.ok("수업 등록 성공");
    }
}
