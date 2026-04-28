package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        LocalDateTime createdAt,
        UserResponse createdBy,
        long taskCount
) {
}
