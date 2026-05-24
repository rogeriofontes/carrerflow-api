package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.ApplicationStatus;
import com.careerflow.domain.entities.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    List<JobApplication> findByUserId(UUID userId);

    List<JobApplication> findByUserIdAndStatus(UUID userId, ApplicationStatus status);

    List<JobApplication> findByCompanyId(UUID companyId);
}
