package db2.todolistapi.service;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.model.TaskItem;
import db2.todolistapi.model.TaskStatus;
import db2.todolistapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTeste {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testSaveTask() {
        TaskItem task = new TaskItem();
        when(taskRepository.save(task)).thenReturn(task);

        TaskItem result = taskService.saveTask(task);

        assertEquals(task, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTasksBySprint() {
        Sprint sprint = new Sprint();
        TaskItem task1 = new TaskItem();
        TaskItem task2 = new TaskItem();
        List<TaskItem> expected = Arrays.asList(task1, task2);

        when(taskRepository.findBySprint(sprint)).thenReturn(expected);

        List<TaskItem> result = taskService.getTasksBySprint(sprint);

        assertEquals(expected, result);
    }

    @Test
    void testUpdateTaskStatus() {
        Long taskId = 1L;
        TaskItem task = new TaskItem();
        task.setStatus(TaskStatus.TODO);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.updateTaskStatus(taskId, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, task.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void testCountTasksByStatus() {
        Sprint sprint = new Sprint();
        TaskStatus status = TaskStatus.IN_PROGRESS;

        when(taskRepository.countBySprintAndStatus(sprint, status)).thenReturn(3L);

        long count = taskService.countTasksByStatus(sprint, status);

        assertEquals(3L, count);
    }

    @Test
    void testSumStoryPointsBySprint() {
        Sprint sprint = new Sprint();

        when(taskRepository.sumStoryPointsBySprint(sprint)).thenReturn(13);

        int sum = taskService.sumStoryPointsBySprint(sprint);

        assertEquals(13, sum);
    }
}
