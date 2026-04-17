package com.example.reservation.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다")
    private String name;

    @Pattern(regexp = "^01[0-9]-[0-9]{4}-[0-9]{4}$",
             message = "휴대폰 번호는 010-1234-5678 형식이어야 합니다.")
    private String phone;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$",
            message = "비밀번호는 영문과 숫자를 포함해야 합니다")
    private String password;
}
