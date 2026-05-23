package com.careerflow.application.services;

import com.careerflow.application.dto.ChallengeRequest;
import com.careerflow.application.dto.ChallengeResponse;
import com.careerflow.domain.entities.Challenge;
import com.careerflow.domain.entities.Company;
import com.careerflow.domain.repositories.ChallengeRepository;
import com.careerflow.domain.repositories.CompanyRepository;
import com.careerflow.domain.valueobjects.Difficulty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public ChallengeResponse create(UUID companyUserId, ChallengeRequest request) {
        Company company = companyRepository.findByUserId(companyUserId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found for user: " + companyUserId));

        Challenge challenge = Challenge.builder()
                .title(request.title())
                .description(request.description())
                .difficulty(request.difficulty())
                .skills(request.skills())
                .company(company)
                .build();

        challenge = challengeRepository.save(challenge);
        log.info("Challenge created: {}", challenge.getTitle());
        return toResponse(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponse findById(UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + id));
        return toResponse(challenge);
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> findAll(Pageable pageable) {
        return challengeRepository.findByActiveTrue(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> findByDifficulty(Difficulty difficulty, Pageable pageable) {
        return challengeRepository.findByDifficultyAndActiveTrue(difficulty, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> findBySkills(List<String> skills, Pageable pageable) {
        return challengeRepository.findBySkillsAndActive(skills, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> findByCompany(UUID companyId, Pageable pageable) {
        return challengeRepository.findByCompanyIdAndActiveTrue(companyId, pageable).map(this::toResponse);
    }

    @Transactional
    public void deactivate(UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + id));
        challenge.setActive(false);
        challengeRepository.save(challenge);
    }

    private ChallengeResponse toResponse(Challenge challenge) {
        return new ChallengeResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getDescription(),
                challenge.getDifficulty(),
                challenge.getSkills(),
                challenge.getCompany() != null ? challenge.getCompany().getId() : null,
                challenge.isActive(),
                challenge.getCreatedAt()
        );
    }
}
