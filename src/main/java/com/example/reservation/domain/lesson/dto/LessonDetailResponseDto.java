package com.example.reservation.domain.lesson.dto;

import com.example.reservation.domain.instructor.dto.InstructorDetailResponseDto;
import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.lesson.entity.Lesson;
import com.example.reservation.domain.lesson.entity.LessonStatus;
import com.example.reservation.domain.lesson.entity.LessonType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LessonDetailResponseDto {

    private Long id;
    private String instructorName;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
    private int currentCount;
    private LocalDateTime createdAt;
    private LessonType lessonType;
    private LessonStatus status;

    public static LessonDetailResponseDto from(Lesson lesson) {
        return new LessonDetailResponseDto(
                lesson.getId(),
                lesson.getInstructor().getName(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                lesson.getMaxCapacity(),
                lesson.getCurrentCount(),
                lesson.getCreatedAt(),
                lesson.getLessonType(),
                lesson.getStatus()
        );

    }
}
