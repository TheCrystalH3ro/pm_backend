package dev.hero.projectmanager.dto.response;

public record AuthResult(String token, String username, String role, boolean hasOrganization) {
}
