package com.careerflow.application.dto;

import com.careerflow.domain.entities.InterviewResult;
import com.careerflow.domain.entities.InterviewType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record InterviewRequest(
        @NotNull UUID jobApplicationId,
        @NotNull Instant scheduledAt,
        @NotNull InterviewType type,
        @Size(max = 255) String location,
        @NotNull InterviewResult result,
        @Size(max = 2000) String notes
) {
}
