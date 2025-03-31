package db2.todolistapi.swing.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ReportsCard extends JPanel {
    private final String title;
    private final String value;
    private final String description;
    private final Color backgroundColor;
    private final Color textColor;

    public ReportsCard(String title, String value, String description, Color backgroundColor) {
        this.title = title;
        this.value = value;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.textColor = Color.WHITE;

        initComponents();
        setPreferredSize(new Dimension(200, 120));
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(textColor);
        add(titleLabel, gbc);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(textColor);
        add(valueLabel, gbc);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setForeground(textColor);
        add(descLabel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), 20, 20);

        GradientPaint gradient = new GradientPaint(
                0, 0, backgroundColor.brighter(),
                0, getHeight(), backgroundColor.darker());

        g2d.setPaint(gradient);
        g2d.fill(roundedRectangle);

        g2d.dispose();
    }

    public static ReportsCard createProgressCard(String value, String description) {
        return new ReportsCard("Progresso", value, description, new Color(63, 81, 181)); // Azul
    }

    public static ReportsCard createTasksCard(String value, String description) {
        return new ReportsCard("Tarefas", value, description, new Color(0, 150, 136)); // Verde
    }

    public static ReportsCard createPointsCard(String value, String description) {
        return new ReportsCard("Story Points", value, description, new Color(255, 152, 0)); // Laranja
    }

    public static ReportsCard createTimeCard(String value, String description) {
        return new ReportsCard("Tempo Restante", value, description, new Color(156, 39, 176)); // Roxo
    }
}
