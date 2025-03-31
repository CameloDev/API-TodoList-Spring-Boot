package db2.todolistapi.repository;

import db2.todolistapi.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByActiveTrue();
}
