package dev.hero.projectmanager.dto.response;

import java.util.List;

public record BoardColumnResponse(
        Long id,
        String name,
        Double position,
        List<TaskResponse> tasks
) {
}
