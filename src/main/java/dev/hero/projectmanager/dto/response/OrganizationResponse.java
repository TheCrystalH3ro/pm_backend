package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.Plan;

import java.time.LocalDateTime;

public record OrganizationResponse(
        Long id,
        String name,
        String slug,
        Plan plan,
        LocalDateTime trialEndsAt,
        int memberCount
) {
}
