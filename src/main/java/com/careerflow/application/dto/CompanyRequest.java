package com.careerflow.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        String name,

        @NotBlank(message = "Segment is required")
        String segment,

        String description,

        String website
) {
}
