package ui;

import dao.AeroportDAO;
import dao.EscaleDAO;
import model.Aeroport;
import model.Escale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class EscaleManagerDialog extends JDialog {

    private final int volId;
    private final EscaleDAO escaleDAO = new EscaleDAO();
    private final AeroportDAO aeroportDAO = new AeroportDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Ordre", "Aéroport", "Heure arrivée", "Heure départ"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };

    private final JTable table = new JTable(model);

    public EscaleManagerDialog(Window owner, int volId) {
        super(owner, "Gérer escales (Vol ID=" + volId + ")", ModalityType.APPLICATION_MODAL);
        this.volId = volId;

        setSize(760, 440);
        setLocationRelativeTo(owner);

        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton add = new JButton("➕ Ajouter escale");
        JButton del = new JButton("🗑️ Supprimer escale");

        add.addActionListener(e -> openAddDialog());
        del.addActionListener(e -> onDelete());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        top.add(add);
        top.add(del);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        model.setRowCount(0);

        for (Escale e : escaleDAO.findByVolId(volId)) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getOrdre(),
                    e.getAeroport() != null ? e.getAeroport().getNom() : "-",
                    e.getHeureArrivee() != null ? e.getHeureArrivee().toString() : "-",
                    e.getHeureDepart() != null ? e.getHeureDepart().toString() : "-"
            });
        }
    }

    private void openAddDialog() {
        JComboBox<Aeroport> cbAeroport = new JComboBox<>(aeroportDAO.findAll().toArray(new Aeroport[0]));
        int nextOrdre = escaleDAO.getNextOrdre(volId);
        JSpinner spOrdre = new JSpinner(new SpinnerNumberModel(nextOrdre, 1, 99, 1));

        JSpinner spArr = new JSpinner(new SpinnerDateModel());
        spArr.setEditor(new JSpinner.DateEditor(spArr, "HH:mm"));

        JSpinner spDep = new JSpinner(new SpinnerDateModel());
        spDep.setEditor(new JSpinner.DateEditor(spDep, "HH:mm"));

        JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
        p.add(new JLabel("Aéroport"));
        p.add(cbAeroport);
        p.add(new JLabel("Ordre (unique)"));
        p.add(spOrdre);
        p.add(new JLabel("Heure arrivée"));
        p.add(spArr);
        p.add(new JLabel("Heure départ"));
        p.add(spDep);

        int ok = JOptionPane.showConfirmDialog(this, p, "Ajouter escale", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        Aeroport a = (Aeroport) cbAeroport.getSelectedItem();
        if (a == null) {
            JOptionPane.showMessageDialog(this, "Choisis un aéroport.");
            return;
        }

        int ordre = (Integer) spOrdre.getValue();
        if (escaleDAO.ordreExists(volId, ordre)) {
            JOptionPane.showMessageDialog(this, "Cet ordre est déjà utilisé pour ce vol.");
            return;
        }

        LocalTime hArr = toLocalTime(spArr.getValue());
        LocalTime hDep = toLocalTime(spDep.getValue());

        if (hDep.isBefore(hArr)) {
            JOptionPane.showMessageDialog(this, "Heure départ escale doit être >= heure arrivée escale.");
            return;
        }

        Escale e = new Escale();
        e.setVolId(volId);
        e.setAeroport(a);
        e.setOrdre(ordre);
        e.setHeureArrivee(hArr);
        e.setHeureDepart(hDep);

        escaleDAO.insert(e);
        refresh();
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
            refresh();
        }
    }

    private LocalTime toLocalTime(Object value) {
        Date d = (Date) value;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
    }
}
