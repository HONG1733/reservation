package com.example.reservation.domain.reservation.dto;

import com.example.reservation.domain.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDetailResponseDto {

    private Long reservationId;
    private Long userId;
    private Long lessonId;
    private String lessonTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime cancelledAt;
}
