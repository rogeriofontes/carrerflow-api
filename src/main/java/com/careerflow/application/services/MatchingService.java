package com.careerflow.application.services;

import com.careerflow.application.dto.MatchingRequest;
import com.careerflow.application.dto.StudentProfileResponse;
import com.careerflow.domain.repositories.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final StudentProfileRepository studentProfileRepository;

    @Transactional(readOnly = true)
    public Page<StudentProfileResponse> findMatchingStudents(MatchingRequest request, Pageable pageable) {
        Double minScore = request.minScore() != null ? request.minScore() : 0.0;

        if (request.skills() != null && !request.skills().isEmpty()) {
            log.info("Matching students by skills: {} with min score: {}", request.skills(), minScore);
            return studentProfileRepository.findBySkillsAndMinScore(request.skills(), minScore, pageable)
                    .map(this::toResponse);
        }

        log.info("Matching students with min score: {}", minScore);
        return studentProfileRepository.findByMinScore(minScore, pageable)
                .map(this::toResponse);
    }

    private StudentProfileResponse toResponse(com.careerflow.domain.entities.StudentProfile profile) {
        return new StudentProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getName(),
                profile.getCourse(),
                profile.getInstitution(),
                profile.getSkills(),
                profile.getOverallScore(),
                profile.getChallengesCompleted()
        );
    }
}
