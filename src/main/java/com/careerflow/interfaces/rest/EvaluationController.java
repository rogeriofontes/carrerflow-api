package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.StarEvaluationResponse;
import com.careerflow.application.services.StarEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/evaluations")
@RequiredArgsConstructor
@Tag(name = "Evaluations", description = "STAR evaluation endpoints")
public class EvaluationController {

    private final StarEvaluationService evaluationService;

    @GetMapping("/submission/{submissionId}")
    @Operation(summary = "Get evaluation by submission ID")
    public ResponseEntity<StarEvaluationResponse> findBySubmission(@PathVariable UUID submissionId) {
        return ResponseEntity.ok(evaluationService.findBySubmissionId(submissionId));
    }

    @PostMapping("/submission/{submissionId}/evaluate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Trigger manual evaluation (Admin only)")
    public ResponseEntity<StarEvaluationResponse> evaluate(@PathVariable UUID submissionId) {
        return ResponseEntity.ok(evaluationService.evaluate(submissionId));
    }
}
