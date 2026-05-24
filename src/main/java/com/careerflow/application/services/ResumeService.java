package com.careerflow.application.services;

import com.careerflow.application.dto.ResumeRequest;
import com.careerflow.application.dto.ResumeResponse;
import com.careerflow.domain.entities.Resume;
import com.careerflow.domain.entities.User;
import com.careerflow.domain.repositories.ResumeRepository;
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
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ResumeResponse create(ResumeRequest request) {
        Optional<User> userById = userRepository.findById(request.userId());
        Resume resume = Resume.builder()
                .user(userById.orElse(null))
                .title(request.title())
                .contentUrl(request.contentUrl())
                .build();
        return ResumeResponse.from(resumeRepository.save(resume));
    }

    public ResumeResponse update(UUID id, ResumeRequest request) throws ChangeSetPersister.NotFoundException {
        Optional<User> userById = userRepository.findById(request.userId());
        Resume resume = findEntity(id);
        resume.setUser(userById.orElse(null));
        resume.setTitle(request.title());
        resume.setContentUrl(request.contentUrl());
        return ResumeResponse.from(resume);
    }

    @Transactional(readOnly = true)
    public ResumeResponse findById(UUID id) throws ChangeSetPersister.NotFoundException {
        return ResumeResponse.from(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> findAll(UUID userId) {
        List<Resume> result = userId != null
                ? resumeRepository.findByUserId(userId)
                : resumeRepository.findAll();
        return result.stream().map(ResumeResponse::from).toList();
    }

    public void delete(UUID id) throws ChangeSetPersister.NotFoundException {
        Resume resume = findEntity(id);
        resumeRepository.delete(resume);
    }

    Resume findEntity(UUID id) throws ChangeSetPersister.NotFoundException {
        return resumeRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
