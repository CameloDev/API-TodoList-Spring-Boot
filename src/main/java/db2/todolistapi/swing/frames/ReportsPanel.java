package db2.todolistapi.swing.frames;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.model.TaskStatus;
import db2.todolistapi.service.SprintService;
import db2.todolistapi.service.TaskService;
import db2.todolistapi.swing.components.ReportsCard;
import db2.todolistapi.swing.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReportsPanel extends JPanel {
    private final SprintService sprintService;
    private final TaskService taskService;

    private JComboBox<Sprint> sprintCombo;
    private JPanel cardsPanel;
    private JTextArea detailsArea;

    public ReportsPanel(SprintService sprintService, TaskService taskService) {
        this.sprintService = sprintService;
        this.taskService = taskService;

        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(SwingUtils.createTitleLabel("Relatórios de Sprint"), BorderLayout.WEST);

        sprintCombo = new JComboBox<>();
        sprintCombo.addActionListener(e -> updateReports());
        topPanel.add(sprintCombo, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JScrollPane cardsScroll = new JScrollPane(cardsPanel);
        cardsScroll.setBorder(null);
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(cardsScroll, BorderLayout.CENTER);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Detalhes do Sprint"));
        add(detailsScroll, BorderLayout.SOUTH);
    }

    public void refreshData() {
        sprintCombo.removeAllItems();
        sprintService.getAllSprints().forEach(sprintCombo::addItem);

        if (sprintCombo.getItemCount() > 0) {
            sprintCombo.setSelectedIndex(0);
            updateReports();
        }
    }

    private void updateReports() {
        cardsPanel.removeAll();
        detailsArea.setText("");

        Sprint selectedSprint = (Sprint) sprintCombo.getSelectedItem();
        if (selectedSprint == null) return;

        long totalTasks = taskService.countBySprint(selectedSprint);
        long doneTasks = taskService.countBySprintAndStatus(selectedSprint, TaskStatus.DONE);
        int progressPercent = totalTasks > 0 ? (int) ((doneTasks * 100) / totalTasks) : 0;

        int totalPoints = taskService.sumStoryPointsBySprint(selectedSprint);
        int donePoints = taskService.sumStoryPointsBySprintAndStatus(selectedSprint, TaskStatus.DONE);
        int pointsPercent = totalPoints > 0 ? (donePoints * 100) / totalPoints : 0;

        long daysRemaining = ChronoUnit.DAYS.between(
                java.time.LocalDate.now(),
                selectedSprint.getEndDate()
        );
        daysRemaining = Math.max(0, daysRemaining);

        cardsPanel.add(SwingUtils.createTitledPanel("Progresso",
                new ReportsCard(
                        "Conclusão",
                        progressPercent + "%",
                        doneTasks + "/" + totalTasks + " tarefas",
                        new Color(173, 216, 230) // Azul claro
                )));

        cardsPanel.add(SwingUtils.createTitledPanel("Story Points",
                new ReportsCard(
                        "Pontos concluídos",
                        donePoints + "/" + totalPoints,
                        pointsPercent + "% completos",
                        new Color(144, 238, 144) // Verde claro
                )));

        cardsPanel.add(SwingUtils.createTitledPanel("Tempo",
                new ReportsCard(
                        "Dias restantes",
                        String.valueOf(daysRemaining),
                        selectedSprint.isActive() ? "Sprint ativo" : "Sprint finalizado",
                        new Color(255, 218, 185) // Laranja claro
                )));

        cardsPanel.add(SwingUtils.createTitledPanel("Tarefas por Status",
                new ReportsCard(
                        "Distribuição",
                        "Ver detalhes",
                        "Clique para expandir",
                        new Color(221, 160, 221) // Lilás
                ) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                      // da para adicionar um pequeno grafico aqui
                    }
                }));

        updateDetailsArea(selectedSprint);

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void updateDetailsArea(Sprint sprint) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder details = new StringBuilder();
        details.append(String.format("%-20s: %s\n", "Título", sprint.getTitle()));
        details.append(String.format("%-20s: %s\n", "Data Início", sprint.getStartDate().format(formatter)));
        details.append(String.format("%-20s: %s\n", "Data Fim", sprint.getEndDate().format(formatter)));
        details.append(String.format("%-20s: %s\n", "Status", sprint.isActive() ? "Ativo" : "Finalizado"));
        details.append("\n");

        details.append("Distribuição de Tarefas:\n");
        for (TaskStatus status : TaskStatus.values()) {
            long count = taskService.countBySprintAndStatus(sprint, status);
            details.append(String.format("  %-15s: %d tarefas\n", status, count));
        }

        detailsArea.setText(details.toString());
    }
}