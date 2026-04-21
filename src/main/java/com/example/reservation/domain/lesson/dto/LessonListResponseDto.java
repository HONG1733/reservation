package com.example.reservation.domain.lesson.dto;

import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.lesson.entity.LessonStatus;
import com.example.reservation.domain.lesson.entity.LessonType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LessonListResponseDto {

    private Long id;
    private String title;
    private String instructorName;
    private LocalDateTime startTime;
    private LessonStatus status;


}
