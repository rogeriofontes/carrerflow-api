package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    List<Interview> findByJobApplicationId(UUID jobApplicationId);
}
