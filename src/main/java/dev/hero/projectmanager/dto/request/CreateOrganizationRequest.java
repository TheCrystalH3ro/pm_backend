package dev.hero.projectmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(
        @NotBlank(message = "Organization name cannot be empty")
        @Size(max = 50, message = "Name cannot be exceed 50 characters")
        String name
) {
}
