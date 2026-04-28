package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.Organization;
import dev.hero.projectmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByOrganization(Organization organization);
    List<User> findByOrganizationAndUsernameContainingIgnoreCase(Organization organization, String query);
}
