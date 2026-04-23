package com.example.reservation.domain.reservation.dto;

import com.example.reservation.domain.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationListResponseDto {

    private Long reservationId;
    private Long userId;
    private Long lessonId;
    private String lessonTitle;
    private LocalDateTime startTime;
    private ReservationStatus status;


}
