package com.example.reservation.domain.lesson.dto;

import com.example.reservation.domain.lesson.entity.LessonType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LessonUpdateRequestDto {

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxCapacity;
    private LessonType lessonType;
}
