package db2.todolistapi.swing.components;

import db2.todolistapi.model.TaskItem;

import javax.swing.*;
import java.awt.*;

public class TaskCard extends JPanel {
    public TaskCard(TaskItem task) {
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

        Color bgColor = switch(task.getPriority()) {
            case LOW -> new Color(220, 255, 220);
            case MEDIUM -> new Color(255, 255, 200);
            case HIGH -> new Color(255, 220, 220);
            case CRITICAL -> new Color(255, 200, 200);
        };
        setBackground(bgColor);
    }
}