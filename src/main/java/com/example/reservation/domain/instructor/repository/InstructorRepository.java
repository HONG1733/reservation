package com.example.reservation.domain.instructor.repository;

import com.example.reservation.domain.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByEmail(String email);

}
