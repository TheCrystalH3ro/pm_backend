package dev.hero.projectmanager.dto.request;

import dev.hero.projectmanager.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record CreateTaskRequest(
        @NotBlank(message = "Title cannot be empty")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        String description,

        TaskPriority priority,

        LocalDate dueDate,

        @NotNull(message = "Column ID cannot be empty")
        Long columnId,

        Set<Long> assigneeIds
) {
}
