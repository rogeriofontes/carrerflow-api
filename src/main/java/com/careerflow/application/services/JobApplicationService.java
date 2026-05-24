package com.careerflow.application.services;

import com.careerflow.application.dto.JobApplicationRequest;
import com.careerflow.application.dto.JobApplicationResponse;
import com.careerflow.application.dto.UserResponse;
import com.careerflow.domain.entities.ApplicationStatus;
import com.careerflow.domain.entities.Company;
import com.careerflow.domain.entities.JobApplication;
import com.careerflow.domain.entities.User;
import com.careerflow.domain.repositories.CompanyRepository;
import com.careerflow.domain.repositories.JobApplicationRepository;
import com.careerflow.domain.repositories.UserRepository;
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
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public JobApplicationResponse create(JobApplicationRequest request) {
        Optional<User> userById = userRepository.findById(request.userId());
        Optional<Company> companyById = companyRepository.findById(request.companyId());

        JobApplication application = JobApplication.builder()
                .user(userById.orElse(null))
                .company(companyById.orElse(null))
                .position(request.position())
                .applicationStatus(request.status())
                .appliedAt(request.appliedAt())
                .notes(request.notes())
                .build();

        JobApplication save = jobApplicationRepository.save(application);
        return JobApplicationResponse.from(save);
    }

    public JobApplicationResponse update(UUID id, JobApplicationRequest request) throws ChangeSetPersister.NotFoundException {
        Optional<User> userById = userRepository.findById(request.userId());
        Optional<Company> companyById = companyRepository.findById(request.companyId());

        JobApplication application = findEntity(id);
        application.setUser(userById.orElse(null));
        application.setCompany(companyById.orElse(null));
        application.setPosition(request.position());
        application.setApplicationStatus(request.status());
        application.setAppliedAt(request.appliedAt());
        application.setNotes(request.notes());
        return JobApplicationResponse.from(application);
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse findById(UUID id) throws ChangeSetPersister.NotFoundException {
        return JobApplicationResponse.from(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findAll(UUID userId, ApplicationStatus status) {
        List<JobApplication> result;
        if (userId != null && status != null) {
            result = jobApplicationRepository.findByUserIdAndStatus(userId, status);
        } else if (userId != null) {
            result = jobApplicationRepository.findByUserId(userId);
        } else {
            result = jobApplicationRepository.findAll();
        }
        return result.stream().map(JobApplicationResponse::from).toList();
    }

    public void delete(UUID id) throws ChangeSetPersister.NotFoundException {
        JobApplication application = findEntity(id);
        jobApplicationRepository.delete(application);
    }

    JobApplication findEntity(UUID id) throws ChangeSetPersister.NotFoundException {
        return jobApplicationRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
