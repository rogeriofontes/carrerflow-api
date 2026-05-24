package com.careerflow.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ResumeRequest(
        @NotNull UUID userId,
        @NotBlank @Size(max = 180) String title,
        @NotBlank @Size(max = 500) String contentUrl
) {
}
