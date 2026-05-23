package com.careerflow.application.dto;

import com.careerflow.domain.valueobjects.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Role role,
        boolean active,
        LocalDateTime createdAt
) {
}
