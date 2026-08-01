package com.david.monitoring.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateServiceRequest(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank @Size(min = 5, max = 500) String url
) {}
