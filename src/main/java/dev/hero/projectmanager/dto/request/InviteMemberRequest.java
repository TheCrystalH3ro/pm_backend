package dev.hero.projectmanager.dto.request;

import dev.hero.projectmanager.model.OrgRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InviteMemberRequest(
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,

        OrgRole role
) {
}
