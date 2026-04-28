package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.Invitation;
import dev.hero.projectmanager.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>
{
    Optional<Invitation> findByToken(String token);
    Optional<Invitation> findByEmailAndOrganization(String email, Organization organization);
    List<Invitation> findByOrganizationAndAccepted(Organization organization, boolean accepted);
    void deleteByExpiresAtBefore(LocalDateTime now);
}
