package db2.todolistapi.swing.components;

import db2.todolistapi.model.TaskItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskCard extends JPanel {
    private TaskItem task;

    public TaskCard(TaskItem task, Runnable onDelete) {
        this.task = task;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.add(new JLabel("Status: " + task.getStatus()));
        infoPanel.add(new JLabel("Priority: " + task.getPriority()));
        infoPanel.add(new JLabel("Points: " + task.getStoryPoints()));

        add(titleLabel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Excluir");
        deleteButton.setForeground(Color.RED);
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 10));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a tarefa?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                onDelete.run();
            }
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(deleteButton);

        add(footerPanel, BorderLayout.SOUTH);

        Color bgColor = switch (task.getPriority()) {
            case LOW -> new Color(220, 255, 220);
            case MEDIUM -> new Color(255, 255, 200);
            case HIGH -> new Color(255, 220, 220);
            case CRITICAL -> new Color(255, 200, 200);
        };
        setBackground(bgColor);
    }

    public TaskItem getTask() {
        return task;
    }
}