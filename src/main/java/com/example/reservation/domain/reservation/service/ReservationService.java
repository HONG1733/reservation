package com.example.reservation.domain.reservation.service;

import com.example.reservation.domain.lesson.entity.Lesson;
import com.example.reservation.domain.lesson.repository.LessonRepository;
import com.example.reservation.domain.reservation.dto.*;
import com.example.reservation.domain.reservation.entity.Reservation;
import com.example.reservation.domain.reservation.entity.ReservationStatus;
import com.example.reservation.domain.reservation.repository.ReservationRepository;
import com.example.reservation.domain.user.entity.User;
import com.example.reservation.domain.user.repository.UserRepository;
import com.example.reservation.global.exception.ResourceNotFoundException;
import com.example.reservation.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    // 예약하기
    public ReservationCreateResponseDto reservationCreate(Long userId,ReservationCreateRequestDto request){
        // 1. 사용자 검증
        // userId를 받아서 User가 존재하는지 확인
        User user = validateUser(userId);

        // 2. Lesson 조회
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("수업을 찾을 수 없습니다"));

        
        // 3. Lesson에서 시간 가져옴
        // 수업의 시작/ 종료 시간 가져옴
        LocalDateTime startTime = lesson.getStartTime();
        LocalDateTime endTime = lesson.getEndTime();

        // 4. 시간 검증
        // 수업 시간이 24시간 이상 남아있어야 함
        validateBookingTime(startTime);

        // 5. 정원 초과시 예약 불가
        validateLessonCapacity(lesson);

        // 6. 중복 예약 검증
        // 그 시간에 이미 예약이 있는지 확인
        validateDuplicateBooking(userId, request.getLessonId(), startTime, endTime);


        // 6. 예약 생성
        // 모든 검증 통과 -> 예약 저장
        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .user(user)
                        .lesson(lesson)
                        .status(ReservationStatus.CONFIRMED)
                        .startTime(startTime)
                        .endTime(endTime)
                        .reservedAt(LocalDateTime.now())
                        .build()
        );

        // 수업의 현재 인원 증가
        lesson.setCurrentCount(lesson.getCurrentCount() + 1);
        lessonRepository.save(lesson);

        // 7. ResponseDto로 변환해서 반환
        return new ReservationCreateResponseDto(reservation); // ResponseDto 반환

    }



    // 예약 취소
    public ReservationCancelResponseDto cancelReservation(Long reservationId, Long userId) {
        // 예약 조회& 사용자 본인 검증
        Reservation reservation = validateUserOwnership(reservationId, userId);

        // 예약 상태 검증(CONFIRMED 상태만 취소 가능)
        validateReservationStatus(reservation);

        // 수업 시작 24시간 전까지만 예약 취소 가능
        validateBookingTimeForCancel(reservation);

        // 상태 변경
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        // DB에 저장
        Reservation cancelled = reservationRepository.save(reservation);

        // 3. 정원 감소
        Lesson lesson = reservation.getLesson();
        lesson.setCurrentCount(lesson.getCurrentCount() - 1);
        lessonRepository.save(lesson);

        // 4. ResponseDto로 변환해서 반환
        return new ReservationCancelResponseDto(cancelled);

    }


    // 내 예약 목록 조회
    public List<ReservationListResponseDto> getReservations(Long userId) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getUser().getId().equals(userId))
                .map(reservation -> new ReservationListResponseDto(
                        reservation.getId(),
                        reservation.getUser().getId(),
                        reservation.getLesson().getId(),
                        reservation.getLesson().getTitle(),
                        reservation.getLesson().getStartTime(),
                        reservation.getStatus()
                ))
                .toList();
    }
    // 내 예약 상세 조회
    public ReservationDetailResponseDto getReservation(Long reservationId, Long userId) {
        // 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("예약을 찾을 수 없습니다"));

        // 현재 로그인한 사용자가 소유자인지 검증
        if (!reservation.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("자신의 예약만 조회할 수 있습니다");
        }

        // DTO로 변환해서 반환
        return new ReservationDetailResponseDto(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getLesson().getId(),
                reservation.getLesson().getTitle(),
                reservation.getLesson().getStartTime(),
                reservation.getLesson().getEndTime(),
                reservation.getStatus(),
                reservation.getReservedAt(),
                reservation.getCancelledAt()
        );
    }


    // 검증 메서드들
    private Reservation validateUserOwnership(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다");
        }
        return reservation;
    }

    private void validateReservationStatus(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("예약 중인 상태에서만 취소 가능합니다");
        }
    }

    private void validateBookingTimeForCancel(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayBefore = reservation.getStartTime().minusHours(24);

        if (now.isAfter(oneDayBefore)) {
            throw new IllegalArgumentException("수업 시작 24시간 전까지만 취소 가능합니다");
        }
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
    }

    private void validateBookingTime(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayBefore = startTime.minusHours(24);  // 수업 24시간 전

        // 24시간 이상 남아있어야 함
        if (now.isAfter(oneDayBefore)) {    // 현재 > 24시간전 = 24시간 미만
            throw new IllegalArgumentException("수업 시작 24시간 전까지만 예약 가능합니다");
        }

    }

    private void validateLessonCapacity(Lesson lesson) {
        if (lesson.getCurrentCount() >= lesson.getMaxCapacity()) {
            throw new IllegalArgumentException("정원이 가득 찼습니다");
        }
    }

    private void validateDuplicateBooking(Long userId, Long lessonId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean isDuplicate = reservationRepository.existsByUserIdAndTimeConflict(userId, startTime, endTime);

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 예약된 시간대입니다");
        }
    }


}
