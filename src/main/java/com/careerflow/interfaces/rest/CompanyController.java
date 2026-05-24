package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.CompanyRequest;
import com.careerflow.application.dto.CompanyResponse;
import com.careerflow.application.services.CompanyService;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Company management")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Create company profile")
    public ResponseEntity<CompanyResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.create(principal.user().getId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Update company profile by ID")
    public ResponseEntity<CompanyResponse> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequest request) {

        return ResponseEntity.ok(companyService.update(principal.user().getId(), request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Get current company profile")
    public ResponseEntity<CompanyResponse> getMyCompany(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(companyService.findByUserId(principal.user().getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(summary = "Update current company profile")
    public ResponseEntity<CompanyResponse> updateMyCompany(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.update(principal.user().getId(), request));
    }

    @GetMapping
    @Operation(summary = "List all companies")
    public ResponseEntity<Page<CompanyResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(companyService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID")
    public ResponseEntity<CompanyResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Get company by ID")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
