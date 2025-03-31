package db2.todolistapi.swing.frames;

import db2.todolistapi.model.Sprint;
import db2.todolistapi.service.SprintService;

import javax.swing.*;
import java.awt.*;
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
         // vou ver o que fazer, mas aparece um dialog da sprint
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