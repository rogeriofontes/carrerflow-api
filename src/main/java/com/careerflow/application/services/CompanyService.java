package com.careerflow.application.services;

import com.careerflow.application.dto.CompanyRequest;
import com.careerflow.application.dto.CompanyResponse;
import com.careerflow.domain.entities.Company;
import com.careerflow.domain.entities.User;
import com.careerflow.domain.repositories.CompanyRepository;
import com.careerflow.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional
    public CompanyResponse create(UUID userId, CompanyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Company company = Company.builder()
                .user(user)
                .name(request.name())
                .segment(request.segment())
                .description(request.description())
                .website(request.website())
                .build();

        company = companyRepository.save(company);
        log.info("Company created: {}", company.getName());
        return toResponse(company);
    }

    @Transactional(readOnly = true)
    public CompanyResponse findByUserId(UUID userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found for user: " + userId));
        return toResponse(company);
    }

    @Transactional(readOnly = true)
    public CompanyResponse findById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
        return toResponse(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponse> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public CompanyResponse update(UUID userId, CompanyRequest request) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found for user: " + userId));

        company.setName(request.name());
        company.setSegment(request.segment());
        company.setDescription(request.description());
        company.setWebsite(request.website());

        company = companyRepository.save(company);
        return toResponse(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponse> findAllByUserId(UUID userId, Pageable pageable) {
        return companyRepository.findAllByUserId(userId, pageable).map(this::toResponse);
    }

    @Transactional
    public void deleteById(UUID id) {
        companyRepository.deleteById(id);
    }

    private CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getUser().getId(),
                company.getName(),
                company.getSegment(),
                company.getDescription(),
                company.getWebsite()
        );
    }


}
