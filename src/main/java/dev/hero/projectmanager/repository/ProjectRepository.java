package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.Organization;
import dev.hero.projectmanager.model.Project;
import dev.hero.projectmanager.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>
{
    List<Project> findByOrganizationOrderByCreatedAtDesc(Organization organization);
    List<Project> findByOrganizationAndStatus(Organization organization, ProjectStatus status);
    long countByOrganization(Organization organization);
}
