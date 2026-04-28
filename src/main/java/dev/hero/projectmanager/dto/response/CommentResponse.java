package dev.hero.projectmanager.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        UserResponse author
) {
}
