package com.example.reservation.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String name;

    private String email;

    private String password;

    private String phone;

}
