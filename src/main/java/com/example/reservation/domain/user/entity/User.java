package com.example.reservation.domain.user.entity;

import com.example.reservation.domain.user.dto.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    public void update(UserUpdateRequestDto dto, PasswordEncoder passwordEncoder) {

        if (dto.getName() != null) {
            this.name = dto.getName();
        }

        if (dto.getPhone() != null) {
            this.phone = dto.getPhone();
        }

        if (dto.getPassword() != null) {
            this.password = passwordEncoder.encode(dto.getPassword());
        }



    }

}
