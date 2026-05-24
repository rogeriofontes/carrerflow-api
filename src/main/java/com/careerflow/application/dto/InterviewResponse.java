package com.careerflow.application.dto;

import com.careerflow.domain.entities.Interview;
import com.careerflow.domain.entities.InterviewResult;
import com.careerflow.domain.entities.InterviewType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record InterviewResponse(
        UUID id,
        UUID jobApplicationId,
        Instant scheduledAt,
        InterviewType type,
        String location,
        InterviewResult result,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static InterviewResponse from(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getJobApplication().getId(),
                interview.getScheduledAt(),
                interview.getType(),
                interview.getLocation(),
                interview.getResult(),
                interview.getNotes(),
                interview.getCreatedDate(),
                interview.getLastModifiedDate()
        );
    }
}
