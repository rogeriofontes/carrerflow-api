package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.UserResponse;
import com.careerflow.application.services.UserService;
import com.careerflow.infrastructure.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users (Admin only)")
    public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.findByEmail(principal.getUsername()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID (Admin only)")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user (Admin only)")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
