package db2.todolistapi.swing.frames;

import db2.todolistapi.service.SprintService;
import org.springframework.stereotype.Component;
import db2.todolistapi.service.TaskService;


import javax.swing.*;

@Component
public class MainFrame extends JFrame {
    private final SprintService sprintService;
    private final TaskService taskService;

    private JTabbedPane tabbedPane;
    private SprintPanel sprintPanel;
    private TaskBoardPanel taskBoardPanel;
    private ReportsPanel reportsPanel;

    public MainFrame(SprintService sprintService, TaskService taskService) {
        this.sprintService = sprintService;
        this.taskService = taskService;

        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("Sprint Task Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        sprintPanel = new SprintPanel(sprintService, this::refreshData);
        taskBoardPanel = new TaskBoardPanel(taskService, sprintService);
        reportsPanel = new ReportsPanel(sprintService, taskService);

        tabbedPane.addTab("Sprints", sprintPanel);
        tabbedPane.addTab("Task Board", taskBoardPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        add(tabbedPane);
    }

    private void loadData() {
        refreshData();
    }

    private void refreshData() {
        sprintPanel.refreshData();
        taskBoardPanel.refreshData();
        reportsPanel.refreshData();
    }
}
