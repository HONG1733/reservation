package com.example.reservation.domain.instructor.dto;

import com.example.reservation.domain.instructor.entity.Instructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InstructorDetailResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String profileImageUrl; //사진
    private String speciality;      //전문분야
    private String career;          //경력
    private String certificate;     //자격증
    private String introduction;    //소개글

    public static InstructorDetailResponseDto from(Instructor instructor) {
        return new InstructorDetailResponseDto(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail(),
                instructor.getPhone(),
                instructor.getProfileImageUrl(),
                instructor.getSpeciality(),
                instructor.getCareer(),
                instructor.getCertificate(),
                instructor.getIntroduction()
        );
    }
}
