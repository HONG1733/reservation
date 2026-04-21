package com.example.reservation.domain.lesson.entity;

import com.example.reservation.domain.instructor.entity.Instructor;
import com.example.reservation.domain.lesson.dto.LessonUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private String title;               // 수업명
    private String description;         // 수업설명
    private LocalDateTime startTime;    // 시작시간
    private LocalDateTime endTime;      // 종료시간
    private Integer maxCapacity;        // 최대 정원
    @Builder.Default
    private Integer currentCount = 0;   // 현재 예약인원
    private LocalDateTime createdAt;    // 등록 일시

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;      // 수업 종류
    @Enumerated(EnumType.STRING)
    private LessonStatus status;        // 수업 상태


    public void update(LessonUpdateRequestDto dto) {

        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }

        if (dto.getDescription() != null) {
            this.description = dto.getDescription();
        }

        if (dto.getStartTime() != null) {
            this.startTime = dto.getStartTime();
        }

        if (dto.getEndTime() != null) {
            this.endTime = dto.getEndTime();
        }

        if (dto.getMaxCapacity() != null) {
            this.maxCapacity = dto.getMaxCapacity();
        }
    }
}
