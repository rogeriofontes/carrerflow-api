package com.careerflow.application.dto;

import java.util.List;

public record MatchingRequest(
        List<String> skills,
        Double minScore,
        Integer minChallengesCompleted
) {
}
