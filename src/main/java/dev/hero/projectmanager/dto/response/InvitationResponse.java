package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.OrgRole;

import java.time.LocalDateTime;

public record InvitationResponse(
        Long id,
        String email,
        OrgRole role,
        boolean accepted,
        LocalDateTime expiresAt
) {
}
