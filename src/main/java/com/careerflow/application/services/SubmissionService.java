package com.careerflow.application.services;

import com.careerflow.application.dto.SubmissionRequest;
import com.careerflow.application.dto.SubmissionResponse;
import com.careerflow.application.dto.StarEvaluationResponse;
import com.careerflow.domain.entities.Challenge;
import com.careerflow.domain.entities.StudentProfile;
import com.careerflow.domain.entities.Submission;
import com.careerflow.domain.events.SubmissionCreatedEvent;
import com.careerflow.domain.repositories.ChallengeRepository;
import com.careerflow.domain.repositories.StudentProfileRepository;
import com.careerflow.domain.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ChallengeRepository challengeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SubmissionResponse create(UUID userId, SubmissionRequest request) {
        StudentProfile student = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user: " + userId));

        Challenge challenge = challengeRepository.findById(request.challengeId())
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + request.challengeId()));

        Submission submission = Submission.builder()
                .student(student)
                .challenge(challenge)
                .situation(request.situation())
                .task(request.task())
                .action(request.action())
                .result(request.result())
                .githubUrl(request.githubUrl())
                .build();

        submission = submissionRepository.save(submission);
        log.info("Submission created: {} for challenge: {}", submission.getId(), challenge.getTitle());

        eventPublisher.publishEvent(new SubmissionCreatedEvent(
                submission.getId(), student.getId(), challenge.getId()));

        return toResponse(submission);
    }

    @Transactional(readOnly = true)
    public SubmissionResponse findById(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + id));
        return toResponse(submission);
    }

    @Transactional(readOnly = true)
    public Page<SubmissionResponse> findByStudent(UUID studentId, Pageable pageable) {
        return submissionRepository.findByStudentId(studentId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SubmissionResponse> findByChallenge(UUID challengeId, Pageable pageable) {
        return submissionRepository.findByChallengeId(challengeId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public SubmissionResponse findAll(Pageable pageable) {
        return submissionRepository.findAll(pageable).map(this::toResponse).getContent().stream().findFirst().orElse(null);
    }

    private SubmissionResponse toResponse(Submission submission) {
        StarEvaluationResponse evalResponse = null;
        if (submission.getEvaluation() != null) {
            var eval = submission.getEvaluation();
            evalResponse = new StarEvaluationResponse(
                    eval.getId(),
                    submission.getId(),
                    eval.getSituationScore(),
                    eval.getTaskScore(),
                    eval.getActionScore(),
                    eval.getResultScore(),
                    eval.getFinalScore(),
                    eval.getFeedback(),
                    eval.getEvaluatedAt()
            );
        }

        return new SubmissionResponse(
                submission.getId(),
                submission.getStudent().getId(),
                submission.getChallenge().getId(),
                submission.getSituation(),
                submission.getTask(),
                submission.getAction(),
                submission.getResult(),
                submission.getSubmittedAt(),
                evalResponse,
                submission.getGithubUrl()
        );
    }


}
