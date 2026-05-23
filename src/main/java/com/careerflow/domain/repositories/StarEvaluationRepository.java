package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.StarEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StarEvaluationRepository extends JpaRepository<StarEvaluation, UUID> {

    Optional<StarEvaluation> findBySubmissionId(UUID submissionId);

    @Query("SELECT AVG(se.finalScore) FROM StarEvaluation se WHERE se.submission.student.id = :studentId")
    Optional<Double> findAverageScoreByStudentId(@Param("studentId") UUID studentId);
}
