package com.careerflow.domain.repositories;

import com.careerflow.domain.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByUserId(UUID userId);
    Page<Company> findAllByUserId(UUID userId, Pageable pageable);
}
