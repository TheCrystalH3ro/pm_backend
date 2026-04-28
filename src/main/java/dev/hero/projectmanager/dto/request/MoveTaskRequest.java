package dev.hero.projectmanager.dto.request;


import jakarta.validation.constraints.NotNull;

public record MoveTaskRequest(
        @NotNull Long columnId,
        @NotNull Double position
) {}
