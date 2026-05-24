package com.careerflow.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StudentProfileResponse(
        UUID id,
        UUID userId,
        String name,
        String course,
        String institution,
        List<String> skills,
        Double overallScore,
        Integer challengesCompleted
) {
}
