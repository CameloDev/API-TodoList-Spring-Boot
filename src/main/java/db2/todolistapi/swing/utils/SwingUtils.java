package db2.todolistapi.swing.utils;

import javax.swing.*;
import java.awt.*;

public class SwingUtils {

    /**
     * Configura o look and feel padrão do sistema operacional
     */
    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
        }
    }

    /**
     * Centraliza uma janela na tela
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        window.setLocation(x, y);
    }

    /**
     * Cria um JButton estilizado
     */
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        return button;
    }

    /**
     * Cria um JLabel estilizado para títulos
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return label;
    }

    /**
     * Mostra uma mensagem de diálogo com ícone de informação
     */
    public static void showInfoMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Informação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra uma mensagem de diálogo com ícone de erro
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Cria um painel com um título estilizado
     */
    public static JPanel createTitledPanel(String title, Component content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Cria um JTextField estilizado
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    /**
     * Cria um JTextArea estilizado dentro de um JScrollPane
     */
    public static JScrollPane createStyledTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return scrollPane;
    }
}
