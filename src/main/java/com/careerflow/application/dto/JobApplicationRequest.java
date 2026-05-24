package com.careerflow.application.dto;

import com.careerflow.domain.entities.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record JobApplicationRequest(
        @NotNull UUID userId,
        @NotNull UUID companyId,
        @NotBlank @Size(max = 180) String position,
        @NotNull ApplicationStatus status,
        @NotNull LocalDate appliedAt,
        @Size(max = 2000) String notes
) {
}
