package db2.todolistapi.swing.frames;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.service.SprintService;
import db2.todolistapi.swing.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SprintPanel extends JPanel {
    private final SprintService sprintService;
    private final Runnable refreshCallback;

    private JList<Sprint> sprintList;
    private DefaultListModel<Sprint> listModel;

    public SprintPanel(SprintService sprintService, Runnable refreshCallback) {
        this.sprintService = sprintService;
        this.refreshCallback = refreshCallback;

        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        listModel = new DefaultListModel<>();
        sprintList = new JList<>(listModel);
        sprintList.setCellRenderer(new SprintListRenderer());

        JScrollPane scrollPane = new JScrollPane(sprintList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Sprint");
        addButton.addActionListener(e -> showSprintDialog(null));

        JButton editButton = new JButton("Edit Sprint");
        editButton.addActionListener(e -> {
            Sprint selected = sprintList.getSelectedValue();
            if (selected != null) showSprintDialog(selected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        listModel.clear();
        sprintService.getAllSprints().forEach(listModel::addElement);
    }

    private void showSprintDialog(Sprint sprint) {
        // Criação do diálogo
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                sprint == null ? "Nova Sprint" : "Editar Sprint",
                true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 400);  // Aumentado para melhor visualização
        SwingUtils.centerWindow(dialog);
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes do formulário
        JTextField titleField = SwingUtils.createStyledTextField(25);
        JTextArea descArea = new JTextArea(5, 25);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(300, 100));

        // Componentes de data usando JSpinner
        SpinnerDateModel startModel = new SpinnerDateModel();
        SpinnerDateModel endModel = new SpinnerDateModel();
        JSpinner startSpinner = new JSpinner(startModel);
        JSpinner endSpinner = new JSpinner(endModel);

        // Configura o formato de exibição das datas
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "dd/MM/yyyy"));
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "dd/MM/yyyy"));

        // Componente de status
        JCheckBox activeCheckbox = new JCheckBox("Sprint Ativo");
        activeCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Preenchimento dos campos se estiver editando
        if (sprint != null) {
            titleField.setText(sprint.getTitle());
            descArea.setText(sprint.getDescription());

            // Configura as datas iniciais
            if (sprint.getStartDate() != null) {
                startModel.setValue(java.sql.Date.valueOf(sprint.getStartDate()));
            }
            if (sprint.getEndDate() != null) {
                endModel.setValue(java.sql.Date.valueOf(sprint.getEndDate()));
            }

            activeCheckbox.setSelected(sprint.isActive());
        } else {
            // Valores padrão para nova sprint
            startModel.setValue(new java.sql.Date(System.currentTimeMillis())); // Data atual
            endModel.setValue(java.sql.Date.valueOf(LocalDate.now().plusDays(14))); // 2 semanas depois
        }

        // Adiciona componentes ao formulário
        formPanel.add(new JLabel("Título:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(descScroll);
        formPanel.add(new JLabel("Data Início:"));
        formPanel.add(startSpinner);
        formPanel.add(new JLabel("Data Fim:"));
        formPanel.add(endSpinner);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(activeCheckbox);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        JButton saveButton = SwingUtils.createStyledButton("Salvar", new Color(34, 139, 34)); // Verde
        JButton cancelButton = SwingUtils.createStyledButton("Cancelar", new Color(178, 34, 34)); // Vermelho

        saveButton.addActionListener(e -> {
            try {
                // Validação do título
                if (titleField.getText().trim().isEmpty()) {
                    SwingUtils.showErrorMessage(dialog, "O título da sprint é obrigatório");
                    return;
                }

                // Obtém os valores dos spinners como java.util.Date
                java.util.Date utilStartDate = (java.util.Date) startSpinner.getValue();
                java.util.Date utilEndDate = (java.util.Date) endSpinner.getValue();

                // Converte para java.sql.Date
                java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
                java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());

                // Converte para LocalDate
                LocalDate startDate = sqlStartDate.toLocalDate();
                LocalDate endDate = sqlEndDate.toLocalDate();

                // Validação do intervalo de datas
                if (endDate.isBefore(startDate)) {
                    SwingUtils.showErrorMessage(dialog, "A data final deve ser após a data inicial");
                    return;
                }

                // Prepara o objeto Sprint para salvar
                Sprint sprintToSave = sprint != null ? sprint : new Sprint();
                sprintToSave.setTitle(titleField.getText());
                sprintToSave.setDescription(descArea.getText());
                sprintToSave.setStartDate(startDate);
                sprintToSave.setEndDate(endDate);
                sprintToSave.setActive(activeCheckbox.isSelected());

                // Lógica para desativar outras sprints se esta for ativada
                if (activeCheckbox.isSelected() && (sprint == null || !sprint.isActive())) {
                    sprintService.deactivateOtherSprints(sprintToSave.getId());
                }

                // Salva a sprint
                sprintService.saveSprint(sprintToSave);
                refreshData(); // Atualiza a interface
                dialog.dispose(); // Fecha o diálogo

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtils.showErrorMessage(dialog,
                        "Erro ao salvar sprint: " + ex.getLocalizedMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Adiciona os componentes ao diálogo
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Exibe o diálogo
        dialog.setVisible(true);
    }

    private static class SprintListRenderer extends DefaultListCellRenderer {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Sprint s) {
                String text = String.format("%s (%s - %s) %s",
                        s.getTitle(),
                        s.getStartDate().format(formatter),
                        s.getEndDate().format(formatter),
                        s.isActive() ? "[ACTIVE]" : "");
                setText(text);
            }

            return this;
        }
    }
}