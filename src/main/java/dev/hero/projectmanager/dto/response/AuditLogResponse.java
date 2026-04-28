package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.AuditAction;
import dev.hero.projectmanager.model.EntityType;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        AuditAction action,
        EntityType entityType,
        Long entityId,
        String details,
        LocalDateTime createdAt,
        UserResponse performedBy
) {
}
