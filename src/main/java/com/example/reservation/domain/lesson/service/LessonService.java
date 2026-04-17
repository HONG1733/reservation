package com.example.reservation.domain.lesson.service;

import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.instructor.repository.InstructorRepository;
import com.example.reservation.domain.lesson.dto.LessonRequestDto;
import com.example.reservation.domain.lesson.entity.Lesson;
import com.example.reservation.domain.lesson.entity.LessonStatus;
import com.example.reservation.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final InstructorRepository instructorRepository;

    // 수업 등록
    public void createLesson(String email, LessonRequestDto dto) {

        // 트레이너 조회
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("트레이너 아님"));

        // 1. 시작 시간이 종료 시간보다 빨라야한다
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        // 2. 시작 시간이 48시간 이후여야 한다
        if (dto.getStartTime().isBefore(LocalDateTime.now().plusHours(48))) {
            throw new IllegalArgumentException("수업은 48시간 이후부터 등록 가능합니다.");

        }

        Lesson lesson = Lesson.builder()
                .instructor(instructor)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .maxCapacity(dto.getMaxCapacity())
                .lessonType(dto.getLessonType())
                .Status(LessonStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        lessonRepository.save(lesson);
    }

    // 수업 목록 조회
//    public List<LessonResponseDto> getLessons() {
//        return null;
//    }

    // 수업 상세 조회
//    public LessonResponseDto getLesson(Long lessonId) {
//        return null;
//    }

    // 수업 수정
    public void updateLesson(Long instructorId, Long lessonId, LessonRequestDto dto) {

    }

    // 수업 삭제
    public void deleteLesson(Long instructorId, Long lessonId) {

    }
}
