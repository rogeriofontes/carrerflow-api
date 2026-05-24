package com.careerflow.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubmissionRequest(
        @NotNull(message = "Challenge ID is required")
        UUID challengeId,

        @NotBlank(message = "Situation is required")
        String situation,

        @NotBlank(message = "Task is required")
        String task,

        @NotBlank(message = "Action is required")
        String action,

        @NotBlank(message = "Result is required")
        String result,

        @NotBlank(message = "GitHub URL is required")
        String githubUrl
) {
}
