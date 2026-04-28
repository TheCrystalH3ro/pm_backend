package dev.hero.projectmanager.dto.request;

import dev.hero.projectmanager.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;

public record UpdateTaskRequest(
        @NotBlank(message = "Title cannot be empty")
        String title,

        String description,

        TaskPriority priority,

        LocalDate dueDate,

        Set<Long> assigneeIds
) {
}
