package com.careerflow.application.usecases;

import com.careerflow.application.services.StudentProfileService;
import com.careerflow.domain.events.EvaluationCompletedEvent;
import com.careerflow.domain.repositories.StarEvaluationRepository;
import com.careerflow.domain.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateStudentScoreUseCase {

    private final StarEvaluationRepository evaluationRepository;
    private final SubmissionRepository submissionRepository;
    private final StudentProfileService studentProfileService;

    @Async
    @EventListener
    @Transactional
    public void handle(EvaluationCompletedEvent event) {
        log.info("Updating score for student: {}", event.studentId());

        Double averageScore = evaluationRepository.findAverageScoreByStudentId(event.studentId())
                .orElse(0.0);

        long completedChallenges = submissionRepository.countByStudentId(event.studentId());

        studentProfileService.updateScore(event.studentId(), averageScore, (int) completedChallenges);

        log.info("Student {} score updated to {}", event.studentId(), averageScore);
    }
}
