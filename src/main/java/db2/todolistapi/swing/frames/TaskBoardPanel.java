package db2.todolistapi.swing.frames;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.model.TaskItem;
import db2.todolistapi.model.TaskStatus;
import db2.todolistapi.service.SprintService;
import db2.todolistapi.service.TaskService;
import db2.todolistapi.swing.components.TaskCard;
import javax.swing.*;
import java.awt.*;

public class TaskBoardPanel extends JPanel {
    private final TaskService taskService;
    private final SprintService sprintService;

    private JPanel boardPanel;
    private Sprint currentSprint;

    public TaskBoardPanel(TaskService taskService, SprintService sprintService) {
        this.taskService = taskService;
        this.sprintService = sprintService;

        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        currentSprint = sprintService.getActiveSprint();

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel sprintTitle = new JLabel(currentSprint != null ?
                currentSprint.getTitle() : "No active sprint");
        sprintTitle.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(sprintTitle, BorderLayout.CENTER);

        JComboBox<Sprint> sprintSelector = new JComboBox<>();
        sprintService.getAllSprints().forEach(sprintSelector::addItem);
        sprintSelector.addActionListener(e -> {
            currentSprint = (Sprint) sprintSelector.getSelectedItem();
            refreshBoard();
        });
        headerPanel.add(sprintSelector, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(1, 4));
        add(boardPanel, BorderLayout.CENTER);

        refreshBoard();
    }

    private void refreshBoard() {
        boardPanel.removeAll();

        if (currentSprint == null) {
            boardPanel.add(new JLabel("No sprint selected", SwingConstants.CENTER));
            return;
        }

        for (TaskStatus status : TaskStatus.values()) {
            JPanel column = createStatusColumn(status);
            boardPanel.add(column);
        }

        revalidate();
        repaint();
    }

    private JPanel createStatusColumn(TaskStatus status) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBorder(BorderFactory.createTitledBorder(status.toString()));

        taskService.getTasksBySprintAndStatus(currentSprint, status).forEach((TaskItem taskItem) -> {
            TaskCard card = new TaskCard(taskItem);
            column.add(card);
            column.add(Box.createVerticalStrut(10));
        });

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> showTaskDialog(null, status));
        column.add(addButton);

        return column;
    }

    private void showTaskDialog(TaskItem task, TaskStatus status) {
        // Implementar diálogo para adicionar/editar tarefas // fazer ate proxima semana
    }

    public void refreshData() {
        currentSprint = sprintService.getActiveSprint();
        refreshBoard();
    }
}
