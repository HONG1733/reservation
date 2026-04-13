package com.example.reservation.domain.instructor.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InstructorRequestDto {

    private String name;
    private String email;
    private String password;
    private String phone;

    private String profileImageUrl; //사진
    private String speciality;      //전문분야
    private String career;          //경력
    private String certificate;     //자격증
    private String introduction;    //소개글
}
