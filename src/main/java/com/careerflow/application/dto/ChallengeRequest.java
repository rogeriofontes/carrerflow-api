package com.careerflow.application.dto;

import com.careerflow.domain.valueobjects.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ChallengeRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Difficulty is required")
        Difficulty difficulty,

        @NotEmpty(message = "At least one skill is required")
        List<String> skills,

        @NotNull(message = "Company ID is required")
        UUID companyId
) {
}
