package com.careerflow.application.dto;

import java.util.List;
import java.util.UUID;

public record TopStudentResponse(
        UUID studentId,
        String name,
        Double overallScore,
        Integer challengesCompleted,
        List<String> skills
) {
}
