package dev.hero.projectmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateColumnRequest(
        @NotBlank(message = "Column name cannot be empty")
        String name
) {
}
