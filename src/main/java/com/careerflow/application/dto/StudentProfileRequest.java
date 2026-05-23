package com.careerflow.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record StudentProfileRequest(
        @NotBlank(message = "Course is required")
        String course,

        @NotBlank(message = "Institution is required")
        String institution,

        @NotEmpty(message = "At least one skill is required")
        List<String> skills
) {
}
