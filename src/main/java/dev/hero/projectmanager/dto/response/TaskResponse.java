package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.TaskPriority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskPriority priority,
        Double position,
        LocalDate dueDate,
        LocalDateTime createdAt,
        Long columnId,
        UserResponse createdBy,
        Set<UserResponse> assignees
) {
}
