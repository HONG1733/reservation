package com.example.reservation.domain.reservation.service;

import com.example.reservation.domain.lesson.entity.Lesson;
import com.example.reservation.domain.lesson.repository.LessonRepository;
import com.example.reservation.domain.reservation.dto.ReservationCancelResponseDto;
import com.example.reservation.domain.reservation.dto.ReservationCreateRequestDto;
import com.example.reservation.domain.reservation.dto.ReservationCreateResponseDto;
import com.example.reservation.domain.reservation.entity.Reservation;
import com.example.reservation.domain.reservation.entity.ReservationStatus;
import com.example.reservation.domain.reservation.repository.ReservationRepository;
import com.example.reservation.domain.user.entity.User;
import com.example.reservation.domain.user.repository.UserRepository;
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
        // lessonId로 수업 정보 조회
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("수업을 찾을 수 없습니다"));

        
        // 3. Lesson에서 시간 가져옴
        // 수업의 시작/ 종료 시간 가져옴
        LocalDateTime startTime = lesson.getStartTime();
        LocalDateTime endTime = lesson.getEndTime();

        // 4. 시간 검증
        // 수업 시간이 24시간 이상 남아있어야 함
        validateBookingTime(startTime, endTime);
        
        validateLessonCapacity(lesson);

        // 5. 중복 예약 검증
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

    // 검증 메서드들

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
    }
    private void validateBookingTime(LocalDateTime startTime, LocalDateTime endTime) {
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

    // 예약 취소
//    public ReservationCancelResponseDto reservationCancel(Long reservationId) {
//        // 예약 조회
//        Reservation reservation = reservationRepository.findById(reservationId)
//                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다"));
//
//        // 수업 시간 체크
//        // 수업 시작 24시간 전까지만 예약 취소 가능
//
//        // 취소 처리
//    }

    // 내 예약 조회

}
