package db2.todolistapi.service;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.repository.SprintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintService {
    private final SprintRepository sprintRepository;

    public SprintService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    public Sprint saveSprint(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    public Sprint getActiveSprint() {
        return sprintRepository.findByActiveTrue().stream().findFirst().orElse(null);
    }

    public void deactivateOtherSprints(Long currentSprintId) {
        sprintRepository.findByActiveTrue().forEach(sprint -> {
            if (!sprint.getId().equals(currentSprintId)) {
                sprint.setActive(false);
                sprintRepository.save(sprint);
            }
        });
    }
}
