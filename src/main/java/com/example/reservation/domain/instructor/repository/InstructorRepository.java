package com.example.reservation.domain.instructor.repository;

import com.example.reservation.domain.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByEmail(String email);

    Optional<Instructor> findByEmail(String email);
}
