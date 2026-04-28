package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.OrgRole;

public record UserResponse(
        Long id,
        String username,
        String email,
        OrgRole orgRole
) {
}
