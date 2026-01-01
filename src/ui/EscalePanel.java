package ui;

import dao.EscaleDAO;
import model.Escale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EscalePanel extends JPanel {

    private final EscaleDAO escaleDAO = new EscaleDAO();

    private final DefaultTableModel model;
    private final JTable table;

    public EscalePanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel page = UiTheme.page();

        // ---------- TITRE + ACTIONS TOP ----------
        JLabel title = new JLabel("Escales");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UiTheme.TEXT_DARK);

        JButton refreshBtn = UiTheme.button("🔄 Actualiser", UiTheme.PRIMARY);
        JButton deleteBtn  = UiTheme.button("🗑️ Supprimer", UiTheme.DANGER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(refreshBtn);
        right.add(deleteBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[]{
                "ID", "Vol ID", "Ordre", "Aéroport", "Heure arrivée", "Heure départ"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(34);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(226, 232, 240));
        table.getTableHeader().setForeground(new Color(15, 23, 42));
        table.setGridColor(new Color(226, 232, 240));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);

        // ---------- ACTIONS ----------
        refreshBtn.addActionListener(e -> loadAll());
        deleteBtn.addActionListener(e -> onDelete());

        // ---------- ASSEMBLAGE ----------
        page.add(top, BorderLayout.NORTH);
        page.add(UiTheme.card(sp), BorderLayout.CENTER);

        add(page, BorderLayout.CENTER);

        loadAll();
    }

    private void loadAll() {
        model.setRowCount(0);
        for (Escale e : escaleDAO.findAll()) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getVolId(),
                    e.getOrdre(),
                    (e.getAeroport() != null ? e.getAeroport().getNom() : "-"),
                    (e.getHeureArrivee() != null ? e.getHeureArrivee().toString() : "-"),
                    (e.getHeureDepart() != null ? e.getHeureDepart().toString() : "-")
            });
        }
        model.fireTableDataChanged();
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionne une escale.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int ok = JOptionPane.showConfirmDialog(
                this,
                "Supprimer cette escale ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (ok == JOptionPane.YES_OPTION) {
            escaleDAO.delete(id);
            loadAll();
        }
    }
}
