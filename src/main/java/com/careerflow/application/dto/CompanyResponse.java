package com.careerflow.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResponse(
        UUID id,
        UUID userId,
        String name,
        String segment,
        String description,
        String website
) {
}
