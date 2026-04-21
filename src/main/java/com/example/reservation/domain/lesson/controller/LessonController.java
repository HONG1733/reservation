package com.example.reservation.domain.lesson.controller;

import com.example.reservation.domain.lesson.dto.LessonDetailResponseDto;
import com.example.reservation.domain.lesson.dto.LessonListResponseDto;
import com.example.reservation.domain.lesson.dto.LessonCreateRequestDto;
import com.example.reservation.domain.lesson.dto.LessonUpdateRequestDto;
import com.example.reservation.domain.lesson.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;


    //수업 등록
//    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "수업 등록", description = "강사 본인만 등록 가능")
    @PostMapping
    public ResponseEntity<String> create(
            @AuthenticationPrincipal User user,
            @RequestBody LessonCreateRequestDto dto) {

        String email = user.getUsername();

        lessonService.createLesson(email, dto);
        return ResponseEntity.ok("수업 등록 성공");
    }

    //수업 수정
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "수업 수정", description = "강사 본인만 수정 가능")
    @PutMapping("/{lessonId}")
    public ResponseEntity<String> update(
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User user,
            @RequestBody LessonUpdateRequestDto dto) {

        String email = user.getUsername();

        lessonService.updateLesson(lessonId, email, dto);
        return ResponseEntity.ok("수업 수정 성공");
    }

    // 수업 삭제
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "수업 삭제", description = "강사 본인만 삭제 가능")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<String> delete(
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User user) {

        String email = user.getUsername();

        lessonService.deleteLesson(lessonId, email);
        return ResponseEntity.ok("수업 삭제 성공");
    }

    // 수업 목록 조회
    @Operation(summary = "수업 목록 조회", description = "수업 목록 조회 가능")
    @GetMapping
    public ResponseEntity<List<LessonListResponseDto>> getLessonList(){
        return ResponseEntity.ok(lessonService.getLessonList());
    }

    // 수업 상세 조회
    @Operation(summary = "수업 상세 조회", description = "수업 상세 조회 가능")
    @GetMapping("/{id}")
    public ResponseEntity<LessonDetailResponseDto> getLessonDetail(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonDetail(id));
    }
}
