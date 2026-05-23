package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.MatchingRequest;
import com.careerflow.application.dto.StudentProfileResponse;
import com.careerflow.application.services.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Student-company matching")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Search matching students")
    public ResponseEntity<Page<StudentProfileResponse>> searchStudents(
            @RequestBody MatchingRequest request, Pageable pageable) {
        return ResponseEntity.ok(matchingService.findMatchingStudents(request, pageable));
    }
}
