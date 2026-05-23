package com.careerflow.application.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UserResponse user
) {
    public AuthResponse(String accessToken, String refreshToken, Long expiresIn, UserResponse user) {
        this(accessToken, refreshToken, "Bearer", expiresIn, user);
    }
}
