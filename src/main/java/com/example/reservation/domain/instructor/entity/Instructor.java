package com.example.reservation.domain.instructor.entity;

import com.example.reservation.domain.user.entity.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "instructors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String profileImageUrl; //사진
    private String speciality;      //전문분야
    private String career;          //경력
    private String certificate;     //자격증
    private String introduction;    //소개글
    private LocalDateTime createdAt;//등록일

}
