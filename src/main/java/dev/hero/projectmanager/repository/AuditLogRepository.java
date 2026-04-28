package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.AuditLog;
import dev.hero.projectmanager.model.EntityType;
import dev.hero.projectmanager.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>
{
    List<AuditLog> findByOrganizationOrderByCreatedAtDesc(Organization organization);
    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(EntityType entityType, Long entityId);

    @Query("""
        SELECT a FROM AuditLog a
        WHERE a.organization = :org
        ORDER BY a.createdAt DESC
        LIMIT :limit
    """)
    List<AuditLog> findRecentActivity(
        @Param("org") Organization org,
        @Param("limit") int limit
    );
}
