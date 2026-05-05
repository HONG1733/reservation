package com.example.reservation.domain.reservation.controller;

import com.example.reservation.domain.reservation.dto.ReservationCancelResponseDto;
import com.example.reservation.domain.reservation.dto.ReservationCreateRequestDto;
import com.example.reservation.domain.reservation.dto.ReservationCreateResponseDto;
import com.example.reservation.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약 하기", description = "회원만 예약 가능")
    @PostMapping
    public ResponseEntity<ReservationCreateResponseDto> createReservation(
            @RequestBody ReservationCreateRequestDto request,
            @AuthenticationPrincipal UserDetails user
    ) {
        Long userId = Long.parseLong(user.getUsername());
        ReservationCreateResponseDto result =
                reservationService.reservationCreate(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "예약 취소", description = "본인 예약만 취소 가능")
    @PatchMapping("/{reservationId}")
    public ReservationCancelResponseDto cancelReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails user
    ) {
        Long userId = Long.parseLong(user.getUsername());
        return reservationService.cancelReservation(reservationId, userId);
    }

    @Operation(summary = "예약 목록 조회", description = "본인 예약 목록 조회 가능")
    @GetMapping
    public ResponseEntity<?> getReservations(
            @AuthenticationPrincipal UserDetails userDetails) {

        // JWT 토큰에서 현재 로그인한 사용자 ID 추출
        Long userId = Long.parseLong(userDetails.getUsername());

        return ResponseEntity.ok(reservationService.getReservations(userId));
    }

    @Operation(summary = "예약 상세 조회", description = "본인 예약 상세 조회 가능")
    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(reservationService.getReservation(reservationId, userId));
    }
}
