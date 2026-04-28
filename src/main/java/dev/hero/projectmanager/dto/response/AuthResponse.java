package dev.hero.projectmanager.dto.response;

public record AuthResponse(String username, String role, boolean hasOrganization) {
}
