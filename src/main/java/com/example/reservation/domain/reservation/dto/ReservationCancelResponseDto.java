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
public class ReservationCancelResponseDto {
    private Long reservationId;
    private ReservationStatus status;  // CANCELLED
    private LocalDateTime cancelledAt;

    // Reservation 엔티티에서 DTO로 변환
    public ReservationCancelResponseDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.status = reservation.getStatus();
        this.cancelledAt = reservation.getCancelledAt();
    }
}
