package com.careerflow.application.services;

import com.careerflow.application.dto.DashboardResponse;
import com.careerflow.application.dto.TopStudentResponse;
import com.careerflow.domain.entities.StudentProfile;
import com.careerflow.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final StudentProfileRepository studentProfileRepository;
    private final CompanyRepository companyRepository;
    private final ChallengeRepository challengeRepository;
    private final SubmissionRepository submissionRepository;
    private final StarEvaluationRepository evaluationRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        long totalStudents = studentProfileRepository.count();
        long totalCompanies = companyRepository.count();
        long totalChallenges = challengeRepository.count();
        long totalSubmissions = submissionRepository.count();

        Double averageScore = evaluationRepository.findAll().stream()
                .mapToDouble(e -> e.getFinalScore())
                .average()
                .orElse(0.0);

        Map<String, Long> submissionsByDifficulty = new HashMap<>();
        submissionsByDifficulty.put("EASY", 0L);
        submissionsByDifficulty.put("MEDIUM", 0L);
        submissionsByDifficulty.put("HARD", 0L);
        submissionsByDifficulty.put("EXPERT", 0L);

        List<TopStudentResponse> topStudents = studentProfileRepository
                .findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "overallScore")))
                .getContent()
                .stream()
                .map(this::toTopStudent)
                .toList();

        return new DashboardResponse(
                totalStudents,
                totalCompanies,
                totalChallenges,
                totalSubmissions,
                Math.round(averageScore * 100.0) / 100.0,
                submissionsByDifficulty,
                topStudents
        );
    }

    private TopStudentResponse toTopStudent(StudentProfile profile) {
        return new TopStudentResponse(
                profile.getId(),
                profile.getUser().getName(),
                profile.getOverallScore(),
                profile.getChallengesCompleted(),
                profile.getSkills()
        );
    }
}
