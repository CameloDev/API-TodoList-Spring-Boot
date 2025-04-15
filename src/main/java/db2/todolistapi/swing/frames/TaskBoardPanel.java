package db2.todolistapi.swing.frames;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.model.TaskItem;
import db2.todolistapi.model.TaskPriority;
import db2.todolistapi.model.TaskStatus;
import db2.todolistapi.service.SprintService;
import db2.todolistapi.service.TaskService;
import db2.todolistapi.swing.components.TaskCard;
import db2.todolistapi.swing.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class TaskBoardPanel extends JPanel {
    private final TaskService taskService;
    private final SprintService sprintService;

    private JLabel sprintTitle;
    private JPanel boardPanel;

    private Sprint currentSprint;
    private JComboBox<Sprint> sprintComboBox;

    public TaskBoardPanel(TaskService taskService, SprintService sprintService) {
        this.taskService = taskService;
        this.sprintService = sprintService;

        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        currentSprint = sprintService.getActiveSprint();

        JPanel headerPanel = new JPanel(new BorderLayout());
        sprintTitle = new JLabel(currentSprint != null ? currentSprint.getTitle() : "No active sprint");
        sprintTitle.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(sprintTitle, BorderLayout.CENTER);

        sprintComboBox = new JComboBox<>();
        sprintService.getAllSprints().forEach(sprintComboBox::addItem);
        sprintComboBox.addActionListener(e -> {
            currentSprint = (Sprint) sprintComboBox.getSelectedItem();
            refreshBoard();
        });
        headerPanel.add(sprintComboBox, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(1, 4));
        add(boardPanel, BorderLayout.CENTER);

        refreshBoard();
    }

    private void refreshBoard() {
        boardPanel.removeAll();

        sprintTitle.setText(currentSprint != null ? currentSprint.getTitle() : "No active sprint");

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
            TaskCard card = new TaskCard(taskItem, () -> {
                taskService.deleteTask(taskItem);
                refreshData();
            });
            column.add(card);
            column.add(Box.createVerticalStrut(10));
        });

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> showTaskDialog(null, status));
        column.add(addButton);

        return column;
    }

    private void showTaskDialog(TaskItem task, TaskStatus status) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                task == null ? "Nova Tarefa" : "Editar Tarefa",
                true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        SwingUtils.centerWindow(dialog);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField titleField = SwingUtils.createStyledTextField(25);
        JTextArea descArea = new JTextArea(5, 25);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(300, 100));

        JComboBox<TaskPriority> priorityCombo = new JComboBox<>(TaskPriority.values());
        JSpinner pointsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        if (task != null) {
            titleField.setText(task.getTitle());
            descArea.setText(task.getDescription());
            priorityCombo.setSelectedItem(task.getPriority());
            pointsSpinner.setValue(task.getStoryPoints());
        }

        formPanel.add(new JLabel("Título*:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(descScroll);
        formPanel.add(new JLabel("Prioridade*:"));
        formPanel.add(priorityCombo);
        formPanel.add(new JLabel("Story Points*:"));
        formPanel.add(pointsSpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        JButton saveButton = SwingUtils.createStyledButton("Salvar", new Color(34, 139, 34));
        JButton cancelButton = SwingUtils.createStyledButton("Cancelar", new Color(178, 34, 34));

        saveButton.addActionListener(e -> {
            try {
                if (titleField.getText().trim().isEmpty()) {
                    SwingUtils.showErrorMessage(dialog, "O título da tarefa é obrigatório");
                    return;
                }

                TaskItem taskToSave = task != null ? task : new TaskItem();
                taskToSave.setTitle(titleField.getText());
                taskToSave.setDescription(descArea.getText());
                taskToSave.setPriority((TaskPriority) priorityCombo.getSelectedItem());
                taskToSave.setStoryPoints((Integer) pointsSpinner.getValue());
                taskToSave.setStatus(status);
                Sprint selectedSprint = (Sprint) sprintComboBox.getSelectedItem();

                if (task == null) {
                    if (selectedSprint == null) {
                        SwingUtils.showErrorMessage(dialog, "Selecione uma sprint");
                        return;
                    }
                    taskToSave.setSprint(selectedSprint);
                }
                taskService.saveTask(taskToSave);
                refreshData();
                if (selectedSprint != null) {
                    sprintComboBox.setSelectedItem(selectedSprint);
                }
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtils.showErrorMessage(dialog, "Erro ao salvar tarefa: " + ex.getLocalizedMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public void refreshData() {
        Sprint selectedSprint = (Sprint) sprintComboBox.getSelectedItem();

        if (selectedSprint != null) {
            sprintComboBox.removeAllItems();
        }

        sprintService.getAllSprints().forEach(sprintComboBox::addItem);
        if (selectedSprint != null && sprintComboBox.getItemCount() > 0) {
            sprintComboBox.setSelectedItem(selectedSprint);
            currentSprint = selectedSprint;
        } else {
            sprintTitle.setText("No active sprint");
            currentSprint = null;
        }
        System.out.println(currentSprint+  " e " + selectedSprint);
        refreshBoard();
    }
}