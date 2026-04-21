package com.example.reservation.domain.lesson.dto;

import com.example.reservation.domain.lesson.entity.LessonType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateRequestDto {

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
    private LessonType lessonType;

}


//private Long id;
//private Instructor instructor;
//private String title;             수업명
//private String description;       수업설명
//private LocalDateTime startTime;  시작시간
//private LocalDateTime endTime;    종료시간
//private int maxCapacity;          최대 정원
//private int currentCount;         현재 예약인원
//private LocalDateTime createdAt;  등록 일시
//private LessonType lessonType;    수업 종류
//private LessonStatus status;      수업 상태