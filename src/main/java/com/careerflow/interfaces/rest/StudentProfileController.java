package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.StudentProfileRequest;
import com.careerflow.application.dto.StudentProfileResponse;
import com.careerflow.application.services.StudentProfileService;
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
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Student Profiles", description = "Student profile management")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Create student profile")
    public ResponseEntity<StudentProfileResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody StudentProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentProfileService.create(principal.user().getId(), request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get current student profile")
    public ResponseEntity<StudentProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(studentProfileService.findByUserId(principal.user().getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Update current student profile")
    public ResponseEntity<StudentProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody StudentProfileRequest request) {
        return ResponseEntity.ok(studentProfileService.update(principal.user().getId(), request));
    }

    @GetMapping
    @Operation(summary = "List all student profiles")
    public ResponseEntity<Page<StudentProfileResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(studentProfileService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student profile by ID")
    public ResponseEntity<StudentProfileResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentProfileService.findById(id));
    }
}
