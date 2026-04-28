package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.BoardColumn;
import dev.hero.projectmanager.model.Organization;
import dev.hero.projectmanager.model.Project;
import dev.hero.projectmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{
    List<Task> findByColumnOrderByPositionAsc(BoardColumn column);

    @Query("""
        SELECT t FROM Task t
        WHERE t.column.project.organization = :org
        AND LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Task> searchByOrganization(
            @Param("org") Organization org,
            @Param("query") String query
    );

    @Query("""
        SELECT t FROM Task t
        WHERE t.column.project = :project
        AND t.dueDate < :date
        AND t.column.name != 'Done'
    """)
    List<Task> findOverdueTasks();

    long countByColumnProject(Project project);
}
