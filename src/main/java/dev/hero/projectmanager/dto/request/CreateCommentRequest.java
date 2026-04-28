package dev.hero.projectmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank(message = "Comment cannot be empty")
        String content
) {
}
