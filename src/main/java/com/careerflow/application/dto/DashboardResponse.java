package com.careerflow.application.dto;

import java.util.List;
import java.util.Map;

public record DashboardResponse(
        Long totalStudents,
        Long totalCompanies,
        Long totalChallenges,
        Long totalSubmissions,
        Double averageScore,
        Map<String, Long> submissionsByDifficulty,
        List<TopStudentResponse> topStudents
) {
}
