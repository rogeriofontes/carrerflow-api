package com.careerflow.domain.events;

import java.util.UUID;

public record SubmissionCreatedEvent(UUID submissionId, UUID studentId, UUID challengeId) {
}
