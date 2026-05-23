package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.Challenge;
import com.careerflow.domain.valueobjects.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {

    Page<Challenge> findByActiveTrue(Pageable pageable);

    Page<Challenge> findByDifficultyAndActiveTrue(Difficulty difficulty, Pageable pageable);

    @Query("SELECT c FROM Challenge c JOIN c.skills s WHERE s IN :skills AND c.active = true")
    Page<Challenge> findBySkillsAndActive(@Param("skills") List<String> skills, Pageable pageable);

    Page<Challenge> findByCompanyIdAndActiveTrue(UUID companyId, Pageable pageable);
}
