package com.example.reservation.domain.lesson.entity;

import com.example.reservation.domain.instructor.entity.Instructor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
@Getter
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

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
    private int currentCount;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;
    @Enumerated(EnumType.STRING)
    private LessonStatus Status;
}
