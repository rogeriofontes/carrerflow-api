package com.careerflow.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubmissionResponse(
        UUID id,
        UUID studentId,
        UUID challengeId,
        String situation,
        String task,
        String action,
        String result,
        LocalDateTime submittedAt,
        StarEvaluationResponse evaluation,
        String githubUrl
) {
}
