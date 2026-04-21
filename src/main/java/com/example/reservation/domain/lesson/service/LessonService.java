package com.example.reservation.domain.lesson.service;

import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.instructor.repository.InstructorRepository;
import com.example.reservation.domain.lesson.dto.LessonDetailResponseDto;
import com.example.reservation.domain.lesson.dto.LessonListResponseDto;
import com.example.reservation.domain.lesson.dto.LessonCreateRequestDto;
import com.example.reservation.domain.lesson.dto.LessonUpdateRequestDto;
import com.example.reservation.domain.lesson.entity.Lesson;
import com.example.reservation.domain.lesson.entity.LessonStatus;
import com.example.reservation.domain.lesson.repository.LessonRepository;
import com.example.reservation.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final InstructorRepository instructorRepository;
    private final ReservationRepository reservationRepository;

    // 수업 등록
    public void createLesson(String email, LessonCreateRequestDto dto) {

        // 트레이너 조회
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("트레이너 아님"));

        // 1. 시작 시간이 종료 시간보다 빨라야한다
        if (dto.getStartTime().isAfter(dto.getEndTime()) ||
            dto.getStartTime().equals(dto.getEndTime())) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 빨라야 합니다");
        }

        // 2. 시작 시간이 48시간 이후여야 한다
        if (dto.getStartTime().isBefore(LocalDateTime.now().plusHours(48))) {
            throw new IllegalArgumentException("수업은 48시간 이후부터 등록 가능합니다");

        }

        Lesson lesson = Lesson.builder()
                .instructor(instructor)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .maxCapacity(dto.getMaxCapacity())
                .lessonType(dto.getLessonType())
                .status(LessonStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        lessonRepository.save(lesson);
    }

    // 수업 수정
    @Transactional
    public void updateLesson(Long lessonId, String email, LessonUpdateRequestDto dto) {
        // 수업 조회
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        // 본인 검증
        if (!email.equals(lesson.getInstructor().getEmail())) {
            throw new IllegalStateException("본인 수업만 수정할 수 있습니다.");
        }
        
        // 시간 조건 체크
        if (LocalDateTime.now().isAfter(lesson.getStartTime().minusHours(24))) {
            throw new IllegalStateException("수업 시간 24시간 전까지만 수정할 수 있습니다.");
        }

        // 예약 유무 확인
        // 예약이 있으면 수업 수정 불가
        //if ()

        // 수정 로직
        lesson.update(dto);

    }

    // 수업 삭제
    @Transactional
    public void deleteLesson(Long lessonId, String email) {
        // 수업 조회
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        // 본인 검증
        if (!lesson.getInstructor().getEmail().equals(email)) {
            throw new IllegalStateException("본인 수업만 삭제할 수 있습니다.");
        }

        // 예약 유무 확인 (추후 구현)
        // if (reservationRepository.existsByLessonId(lessonId)) {
        //     throw new IllegalStateException("예약이 있는 수업은 삭제할 수 없습니다.");
        // }

        lessonRepository.delete(lesson);
    }

    // 수업 목록 조회
    public List<LessonListResponseDto> getLessonList() {

        List<Lesson> lessons = lessonRepository.findAll();

        return lessons.stream()
                .map(lesson -> new LessonListResponseDto(
                        lesson.getId(),
                        lesson.getTitle(),
                        lesson.getInstructor().getName(),
                        lesson.getStartTime(),
                        lesson.getStatus()
                ))
                .toList();
    }

    // 수업 상세 조회
    public LessonDetailResponseDto getLessonDetail(Long lessonId) {

        // 수업 조회
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("존재 하지 않는 수업 입니다"));

        return LessonDetailResponseDto.from(lesson);

    }



}
