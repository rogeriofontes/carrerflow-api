package com.careerflow.application.dto;

import com.careerflow.domain.entities.ApplicationStatus;
import com.careerflow.domain.entities.JobApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record JobApplicationResponse(
        UUID id,
        UUID userId,
        UUID companyId,
        String position,
        ApplicationStatus status,
        LocalDate appliedAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static JobApplicationResponse from(JobApplication app) {
        return new JobApplicationResponse(
                app.getId(),
                app.getUser().getId(),
                app.getCompany().getId(),
                app.getPosition(),
                app.getApplicationStatus(),
                app.getAppliedAt(),
                app.getNotes(),
                app.getCreatedDate(),
                app.getLastModifiedDate()
        );
    }
}
