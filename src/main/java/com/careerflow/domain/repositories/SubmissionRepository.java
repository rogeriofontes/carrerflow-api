package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    Page<Submission> findByStudentId(UUID studentId, Pageable pageable);

    Page<Submission> findByChallengeId(UUID challengeId, Pageable pageable);

    boolean existsByStudentIdAndChallengeId(UUID studentId, UUID challengeId);

    long countByStudentId(UUID studentId);
}
