package dev.hero.projectmanager.dto.request;

import dev.hero.projectmanager.model.ProjectStatus;
import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequest(
        @NotBlank(message = "Project name cannot be empty")
        String name,

        String description,

        ProjectStatus status
) {
}
