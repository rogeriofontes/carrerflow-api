package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.SubmissionRequest;
import com.careerflow.application.dto.SubmissionResponse;
import com.careerflow.application.services.SubmissionService;
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

import java.util.UUID;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
@Tag(name = "Submissions", description = "STAR submission management")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Submit a STAR response")
    public ResponseEntity<SubmissionResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SubmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(submissionService.create(principal.user().getId(), request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get submission by ID")
    public ResponseEntity<SubmissionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(submissionService.findById(id));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "List submissions by student")
    public ResponseEntity<Page<SubmissionResponse>> findByStudent(
            @PathVariable UUID studentId, Pageable pageable) {
        return ResponseEntity.ok(submissionService.findByStudent(studentId, pageable));
    }

    @GetMapping("/challenge/{challengeId}")
    @Operation(summary = "List submissions by challenge")
    public ResponseEntity<Page<SubmissionResponse>> findByChallenge(
            @PathVariable UUID challengeId, Pageable pageable) {
        return ResponseEntity.ok(submissionService.findByChallenge(challengeId, pageable));
    }
}
