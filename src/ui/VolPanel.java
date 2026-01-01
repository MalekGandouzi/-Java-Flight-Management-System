package ui;

import dao.VolDAO;
import model.Vol;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VolPanel extends JPanel {

    private final VolDAO volDAO = new VolDAO();

    private final DefaultTableModel model;
    private final JTable table;

    public VolPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel page = UiTheme.page();

        // ---------- TITRE + AJOUT ----------
        JLabel title = new JLabel("Gestion des Vols");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UiTheme.TEXT_DARK);

        JButton add = UiTheme.button("➕ Ajouter Vol", UiTheme.PRIMARY);
        add.addActionListener(e -> new VolFormDialog(this).setVisible(true));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(add, BorderLayout.EAST);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[]{
                "ID",
                "Départ",
                "Arrivée",
                "Date départ",
                "Heure départ",
                "Date arrivée",
                "Heure arrivée",
                "Réservable"
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

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);

        // ---------- BOUTONS ACTIONS (COLORÉS) ----------
        JButton edit = UiTheme.cardButton("✏️ Modifier", UiTheme.SUCCESS);
        JButton del  = UiTheme.cardButton("🗑️ Supprimer", UiTheme.DANGER);
        JButton esc  = UiTheme.cardButton("🧭 Gérer escales", new Color(124, 58, 237));
        JButton min  = UiTheme.cardButton("⬇️ Min escales", new Color(14, 165, 233));
        JButton max  = UiTheme.cardButton("⬆️ Max escales", new Color(14, 165, 233));

        edit.addActionListener(e -> onEdit());
        del.addActionListener(e -> onDelete());
        esc.addActionListener(e -> onEscales());
        min.addActionListener(e -> showMinMax(true));
        max.addActionListener(e -> showMinMax(false));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(edit);
        actions.add(del);
        actions.add(esc);
        actions.add(min);
        actions.add(max);

        // ---------- ASSEMBLAGE ----------
        page.add(top, BorderLayout.NORTH);
        page.add(UiTheme.card(sp), BorderLayout.CENTER);
        page.add(UiTheme.card(actions), BorderLayout.SOUTH);

        add(page, BorderLayout.CENTER);

        refresh();
    }

    // ---------- MÉTHODES ----------
    public void refresh() {
        model.setRowCount(0);
        for (Vol v : volDAO.findAll()) {
            model.addRow(new Object[]{
                    v.getId(),
                    v.getDepart().getNom(),
                    v.getArrivee().getNom(),
                    v.getDateDepart(),
                    v.getHeureDepart(),
                    v.getDateArrivee(),
                    v.getHeureArrivee(),
                    v.isReservable() ? "✅ Oui" : "❌ Non"
            });
        }
    }

    private Vol getSelectedVol() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) model.getValueAt(row, 0);
        return volDAO.findAll().stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void onEdit() {
        Vol v = getSelectedVol();
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Sélectionne un vol.");
            return;
        }
        new VolFormDialog(this, v).setVisible(true);
    }

    private void onDelete() {
        Vol v = getSelectedVol();
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Sélectionne un vol.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Supprimer ce vol ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            volDAO.delete(v.getId());
            refresh();
        }
    }

   private void onEscales() {
    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Sélectionne un vol d'abord.");
        return;
    }

    int volId = Integer.parseInt(model.getValueAt(row, 0).toString());

    Window owner = SwingUtilities.getWindowAncestor(this);
    new EscaleManagerDialog(owner, volId).setVisible(true);
}


    private void showMinMax(boolean min) {
        Vol v = min ? volDAO.findMinEscales() : volDAO.findMaxEscales();
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Aucun vol trouvé.");
            return;
        }
        JOptionPane.showMessageDialog(
                this,
                "Vol ID: " + v.getId()
                        + "\nDépart: " + v.getDepart().getNom()
                        + "\nArrivée: " + v.getArrivee().getNom(),
                min ? "Vol MIN escales" : "Vol MAX escales",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
