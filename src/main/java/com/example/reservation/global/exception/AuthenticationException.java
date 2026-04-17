package com.example.reservation.global.exception;

// 401 (static 추가!)
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
