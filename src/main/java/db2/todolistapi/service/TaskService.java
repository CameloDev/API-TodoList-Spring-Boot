package db2.todolistapi.service;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.model.TaskItem;
import db2.todolistapi.model.TaskStatus;
import db2.todolistapi.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskItem saveTask(TaskItem task) {
        return taskRepository.save(task);
    }

    public List<TaskItem> getTasksBySprint(Sprint sprint) {
        return taskRepository.findBySprint(sprint);
    }

    @Transactional
    public void updateTaskStatus(Long taskId, TaskStatus status) {
        taskRepository.findById(taskId).ifPresent((TaskItem taskItem) -> {
            taskItem.setStatus(status);
            taskRepository.save(taskItem);
        });
    }

    public long countTasksByStatus(Sprint sprint, TaskStatus status) {
        return taskRepository.countBySprintAndStatus(sprint, status);
    }
    public List<TaskItem> getTasksBySprintAndStatus(Sprint sprint, TaskStatus status) {
        return taskRepository.findBySprintAndStatus(sprint, status);
    }
    @Transactional
    public long countBySprint(Sprint sprint) {
        return taskRepository.countBySprint(sprint);
    }
    @Transactional
    public long countBySprintAndStatus(Sprint sprint, TaskStatus status) {
        return taskRepository.countBySprintAndStatus(sprint, status);
    }
    @Transactional
    public int sumStoryPointsBySprint(Sprint sprint) {
        return taskRepository.sumStoryPointsBySprint(sprint);
    }
    @Transactional
    public int sumStoryPointsBySprintAndStatus(Sprint sprint, TaskStatus status) {
        return taskRepository.sumStoryPointsBySprintAndStatus(sprint, status);
    }

    public void deleteTask(TaskItem task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Tarefa inválida para exclusão");
        }

        taskRepository.delete(task);
    }
}
