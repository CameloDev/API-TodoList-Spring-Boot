package db2.todolistapi.repository;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.model.TaskItem;
import db2.todolistapi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findBySprint(Sprint sprint);
    List<TaskItem> findBySprintAndStatus(Sprint sprint, TaskStatus status);
    long countBySprint(Sprint sprint);

    long countBySprintAndStatus(Sprint sprint, TaskStatus status);


    @Query("SELECT COALESCE(SUM(t.storyPoints), 0) FROM TaskItem t WHERE t.sprint = :sprint")
    int sumStoryPointsBySprint(@Param("sprint") Sprint sprint);

    @Query("SELECT COALESCE(SUM(t.storyPoints), 0) FROM TaskItem t WHERE t.sprint = :sprint AND t.status = :status")
    int sumStoryPointsBySprintAndStatus(@Param("sprint") Sprint sprint,
                                        @Param("status") TaskStatus status);
}