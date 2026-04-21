package com.example.reservation.domain.reservation.dto;

import com.example.reservation.domain.reservation.entity.Reservation;
import com.example.reservation.domain.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateResponseDto {
    private Long reservationId;      // 예약 ID
    private Long userId;             // 사용자 ID
    private Long lessonId;            // 수업 ID
    private LocalDateTime startTime;   // 시작 시간
    private LocalDateTime endTime;     // 종료 시간
    private ReservationStatus status;             // 예약 상태 (예약됨, 취소됨 등)
    private LocalDateTime reservedAt;   // 생성 날짜

    // Reservation 엔티티에서 DTO로 변환하는 생성자
    public ReservationCreateResponseDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.userId = reservation.getUser().getId();
        this.lessonId = reservation.getLesson().getId();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.status = reservation.getStatus();
        this.reservedAt = reservation.getReservedAt();
    }
}
