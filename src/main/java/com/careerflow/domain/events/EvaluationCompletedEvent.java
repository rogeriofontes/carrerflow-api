package com.careerflow.domain.events;

import java.util.UUID;

public record EvaluationCompletedEvent(UUID evaluationId, UUID submissionId, UUID studentId, Double finalScore) {
}
