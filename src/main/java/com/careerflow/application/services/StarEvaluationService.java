package com.careerflow.application.services;

import com.careerflow.application.dto.StarEvaluationResponse;
import com.careerflow.application.gateways.AiEvaluationGateway;
import com.careerflow.domain.entities.Challenge;
import com.careerflow.domain.entities.StarEvaluation;
import com.careerflow.domain.entities.Submission;
import com.careerflow.domain.events.EvaluationCompletedEvent;
import com.careerflow.domain.events.SubmissionCreatedEvent;
import com.careerflow.domain.repositories.StarEvaluationRepository;
import com.careerflow.domain.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StarEvaluationService {

    private final StarEvaluationRepository evaluationRepository;
    private final SubmissionRepository submissionRepository;
    private final AiEvaluationGateway aiEvaluationGateway;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @EventListener
    @Transactional
    public void handleSubmissionCreated(SubmissionCreatedEvent event) {
        log.info("Processing evaluation for submission: {}", event.submissionId());
        try {
            evaluate(event.submissionId());
        } catch (Exception e) {
            log.error("Failed to evaluate submission: {}", event.submissionId(), e);
        }
    }

    @Transactional
    public StarEvaluationResponse evaluate(UUID submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));

        Challenge challenge = submission.getChallenge();

        StarEvaluationResponse aiResult = aiEvaluationGateway.evaluate(submission, challenge);

        Double finalScore = StarEvaluation.calculateFinalScore(
                aiResult.situationScore(),
                aiResult.taskScore(),
                aiResult.actionScore(),
                aiResult.resultScore()
        );

        StarEvaluation evaluation = StarEvaluation.builder()
                .submission(submission)
                .situationScore(aiResult.situationScore())
                .taskScore(aiResult.taskScore())
                .actionScore(aiResult.actionScore())
                .resultScore(aiResult.resultScore())
                .finalScore(finalScore)
                .feedback(aiResult.feedback())
                .build();

        evaluation = evaluationRepository.save(evaluation);
        log.info("Evaluation completed for submission: {} with score: {}", submissionId, finalScore);

        eventPublisher.publishEvent(new EvaluationCompletedEvent(
                evaluation.getId(), submissionId, submission.getStudent().getId(), finalScore));

        return new StarEvaluationResponse(
                evaluation.getId(),
                submissionId,
                evaluation.getSituationScore(),
                evaluation.getTaskScore(),
                evaluation.getActionScore(),
                evaluation.getResultScore(),
                evaluation.getFinalScore(),
                evaluation.getFeedback(),
                evaluation.getEvaluatedAt()
        );
    }

    @Transactional(readOnly = true)
    public StarEvaluationResponse findBySubmissionId(UUID submissionId) {
        StarEvaluation evaluation = evaluationRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found for submission: " + submissionId));

        return new StarEvaluationResponse(
                evaluation.getId(),
                submissionId,
                evaluation.getSituationScore(),
                evaluation.getTaskScore(),
                evaluation.getActionScore(),
                evaluation.getResultScore(),
                evaluation.getFinalScore(),
                evaluation.getFeedback(),
                evaluation.getEvaluatedAt()
        );
    }
}
