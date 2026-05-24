package com.careerflow.application.services;

import com.careerflow.application.dto.InterviewRequest;
import com.careerflow.application.dto.InterviewResponse;
import com.careerflow.domain.entities.Interview;
import com.careerflow.domain.entities.JobApplication;
import com.careerflow.domain.repositories.InterviewRepository;
import com.careerflow.domain.repositories.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepository jobApplicationRepository;

    public InterviewResponse create(InterviewRequest request) {
        Optional<JobApplication> byId = jobApplicationRepository.findById(request.jobApplicationId());

        Interview interview = Interview.builder()
                .jobApplication(byId.orElse(null))
                .scheduledAt(request.scheduledAt())
                .type(request.type())
                .location(request.location())
                .result(request.result())
                .notes(request.notes())
                .build();

        Interview interviewSaved = interviewRepository.save(interview);
        return InterviewResponse.from(interviewSaved);
    }

    public InterviewResponse update(UUID id, InterviewRequest request) throws ChangeSetPersister.NotFoundException {
        Interview interview = findEntity(id);
        interview.setJobApplication(jobApplicationService.findEntity(request.jobApplicationId()));
        interview.setScheduledAt(request.scheduledAt());
        interview.setType(request.type());
        interview.setLocation(request.location());
        interview.setResult(request.result());
        interview.setNotes(request.notes());
        return InterviewResponse.from(interview);
    }

    @Transactional(readOnly = true)
    public InterviewResponse findById(UUID id) throws ChangeSetPersister.NotFoundException {
        return InterviewResponse.from(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> findAll(UUID jobApplicationId) {
        List<Interview> result = jobApplicationId != null
                ? interviewRepository.findByJobApplicationId(jobApplicationId)
                : interviewRepository.findAll();
        return result.stream().map(InterviewResponse::from).toList();
    }

    public void delete(UUID id) throws ChangeSetPersister.NotFoundException {
        Interview interview = findEntity(id);
        interviewRepository.delete(interview);
    }

    Interview findEntity(UUID id) throws ChangeSetPersister.NotFoundException {
        return interviewRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
