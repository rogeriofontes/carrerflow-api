package com.careerflow.application.dto;

import com.careerflow.domain.valueobjects.Difficulty;

import java.util.List;
import java.util.UUID;

public record ChallengeResponse(
        UUID id,
        String title,
        String description,
        Difficulty difficulty,
        List<String> skills,
        UUID companyId,
        boolean active
) {
}
