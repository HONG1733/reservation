package com.example.reservation.domain.reservation.controller;

import com.example.reservation.domain.reservation.dto.ReservationCreateRequestDto;
import com.example.reservation.domain.reservation.dto.ReservationCreateResponseDto;
import com.example.reservation.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationCreateResponseDto create(
            @RequestParam Long userId,
            @RequestBody ReservationCreateRequestDto request) {
        return reservationService.reservationCreate(userId, request);
    }
}
