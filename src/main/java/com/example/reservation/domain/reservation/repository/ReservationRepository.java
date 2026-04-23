package com.example.reservation.domain.reservation.repository;

import com.example.reservation.domain.reservation.entity.Reservation;
import com.example.reservation.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByLessonId(Long lessonId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    boolean existsByUserIdAndTimeConflict(@Param("userId") Long userId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
}
