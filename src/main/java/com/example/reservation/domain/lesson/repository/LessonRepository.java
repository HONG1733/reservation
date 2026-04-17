package com.example.reservation.domain.lesson.repository;

import com.example.reservation.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
