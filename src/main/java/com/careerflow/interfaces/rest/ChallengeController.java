package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.ChallengeRequest;
import com.careerflow.application.dto.ChallengeResponse;
import com.careerflow.application.services.ChallengeService;
import com.careerflow.domain.valueobjects.Difficulty;
import com.careerflow.infrastructure.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
@Tag(name = "Challenges", description = "Challenge management")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Create a new challenge")
    public ResponseEntity<ChallengeResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChallengeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(challengeService.create(principal.user().getId(), request));
    }

    @GetMapping
    @Operation(summary = "List all active challenges")
    public ResponseEntity<Page<ChallengeResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(challengeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get challenge by ID")
    public ResponseEntity<ChallengeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(challengeService.findById(id));
    }

    @GetMapping("/difficulty/{difficulty}")
    @Operation(summary = "Filter challenges by difficulty")
    public ResponseEntity<Page<ChallengeResponse>> findByDifficulty(
            @PathVariable Difficulty difficulty, Pageable pageable) {
        return ResponseEntity.ok(challengeService.findByDifficulty(difficulty, pageable));
    }

    @GetMapping("/skills")
    @Operation(summary = "Filter challenges by skills")
    public ResponseEntity<Page<ChallengeResponse>> findBySkills(
            @RequestParam List<String> skills, Pageable pageable) {
        return ResponseEntity.ok(challengeService.findBySkills(skills, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Deactivate a challenge")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        challengeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
