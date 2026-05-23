package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {

    Optional<StudentProfile> findByUserId(UUID userId);

    @Query("SELECT sp FROM StudentProfile sp WHERE sp.overallScore >= :minScore ORDER BY sp.overallScore DESC")
    Page<StudentProfile> findByMinScore(@Param("minScore") Double minScore, Pageable pageable);

    @Query("SELECT sp FROM StudentProfile sp JOIN sp.skills s WHERE s IN :skills")
    Page<StudentProfile> findBySkillsIn(@Param("skills") List<String> skills, Pageable pageable);

    @Query("SELECT sp FROM StudentProfile sp JOIN sp.skills s WHERE s IN :skills AND sp.overallScore >= :minScore ORDER BY sp.overallScore DESC")
    Page<StudentProfile> findBySkillsAndMinScore(@Param("skills") List<String> skills,
                                                  @Param("minScore") Double minScore,
                                                  Pageable pageable);
}
