package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.BoardColumn;
import dev.hero.projectmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long>
{
    List<BoardColumn> findByProjectOrderByPositionAsc(Project project);
}
