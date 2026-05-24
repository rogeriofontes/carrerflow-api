package com.careerflow.application.services;

import com.careerflow.application.dto.StudentProfileRequest;
import com.careerflow.application.dto.StudentProfileResponse;
import com.careerflow.domain.entities.StudentProfile;
import com.careerflow.domain.entities.User;
import com.careerflow.domain.repositories.StudentProfileRepository;
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
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudentProfileResponse create(UUID userId, StudentProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        studentProfileRepository.findByUserId(userId).ifPresent(p -> {
            throw new IllegalArgumentException("Student profile already exists for user: " + userId);
        });

        StudentProfile profile = StudentProfile.builder()
                .user(user)
                .course(request.course())
                .institution(request.institution())
                .skills(request.skills())
                .build();

        profile = studentProfileRepository.save(profile);
        log.info("Student profile created for user: {}", userId);
        return toResponse(profile);
    }

    @Transactional(readOnly = true)
    public StudentProfileResponse findByUserId(UUID userId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user: " + userId));
        return toResponse(profile);
    }

    @Transactional(readOnly = true)
    public StudentProfileResponse findById(UUID id) {
        StudentProfile profile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found: " + id));
        return toResponse(profile);
    }

    @Transactional(readOnly = true)
    public Page<StudentProfileResponse> findAll(Pageable pageable) {
        return studentProfileRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public StudentProfileResponse update(UUID userId, StudentProfileRequest request) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user: " + userId));

        profile.setCourse(request.course());
        profile.setInstitution(request.institution());
        profile.setSkills(request.skills());

        profile = studentProfileRepository.save(profile);
        return toResponse(profile);
    }

    @Transactional
    public void updateScore(UUID studentId, Double score, int challengesCompleted) {
        StudentProfile profile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found: " + studentId));
        profile.setOverallScore(score);
        profile.setChallengesCompleted(challengesCompleted);
        studentProfileRepository.save(profile);
    }

    private StudentProfileResponse toResponse(StudentProfile profile) {
        return new StudentProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getName(),
                profile.getCourse(),
                profile.getInstitution(),
                profile.getSkills(),
                profile.getOverallScore(),
                profile.getChallengesCompleted()
        );
    }
}
