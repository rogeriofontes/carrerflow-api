package com.careerflow.application.dto;

import com.careerflow.domain.entities.Resume;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResumeResponse(
        UUID id,
        UUID userId,
        String title,
        String contentUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ResumeResponse from(Resume resume) {
        return new ResumeResponse(
                resume.getId(),
                resume.getUser().getId(),
                resume.getTitle(),
                resume.getContentUrl(),
                resume.getCreatedDate(),
                resume.getLastModifiedDate()
        );
    }
}
