package com.careerflow.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StarEvaluationResponse(
        UUID id,
        UUID submissionId,
        Double situationScore,
        Double taskScore,
        Double actionScore,
        Double resultScore,
        Double finalScore,
        String feedback,
        LocalDateTime evaluatedAt
) {
}
