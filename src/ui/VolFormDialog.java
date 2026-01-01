package ui;

import dao.AeroportDAO;
import dao.VolDAO;
import model.Aeroport;
import model.Vol;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class VolFormDialog extends JDialog {

    private final VolDAO volDAO = new VolDAO();
    private final AeroportDAO aeroportDAO = new AeroportDAO();

    // ✅ constructeur pour AJOUT
    public VolFormDialog(VolPanel parent) {
        this(parent, null);
    }

    // ✅ constructeur pour MODIFICATION (existing != null)
    public VolFormDialog(VolPanel parent, Vol existing) {
        setTitle(existing == null ? "Ajouter Vol" : "Modifier Vol");
        setModal(true);
        setSize(430, 420);
        setLocationRelativeTo(parent);

        // --- Champs ---
        JComboBox<Aeroport> cbDepart = new JComboBox<>();
        JComboBox<Aeroport> cbArrivee = new JComboBox<>();
        JCheckBox chkReservable = new JCheckBox("Réservable");

        // Date depart/arrivee
        JSpinner spDateDepart = new JSpinner(new SpinnerDateModel());
        spDateDepart.setEditor(new JSpinner.DateEditor(spDateDepart, "yyyy-MM-dd"));

        JSpinner spDateArrivee = new JSpinner(new SpinnerDateModel());
        spDateArrivee.setEditor(new JSpinner.DateEditor(spDateArrivee, "yyyy-MM-dd"));

        // Heure depart/arrivee
        JSpinner spHeureDepart = new JSpinner(new SpinnerDateModel());
        spHeureDepart.setEditor(new JSpinner.DateEditor(spHeureDepart, "HH:mm"));

        JSpinner spHeureArrivee = new JSpinner(new SpinnerDateModel());
        spHeureArrivee.setEditor(new JSpinner.DateEditor(spHeureArrivee, "HH:mm"));

        // Charger aeroports
        for (Aeroport a : aeroportDAO.findAll()) {
            cbDepart.addItem(a);
            cbArrivee.addItem(a);
        }

        // Pré-remplir si modification
        if (existing != null) {
            selectAeroport(cbDepart, existing.getDepart());
            selectAeroport(cbArrivee, existing.getArrivee());
            chkReservable.setSelected(existing.isReservable());

            if (existing.getDateDepart() != null)
                spDateDepart.setValue(toDate(existing.getDateDepart()));
            if (existing.getDateArrivee() != null)
                spDateArrivee.setValue(toDate(existing.getDateArrivee()));
            if (existing.getHeureDepart() != null)
                spHeureDepart.setValue(toDate(existing.getHeureDepart()));
            if (existing.getHeureArrivee() != null)
                spHeureArrivee.setValue(toDate(existing.getHeureArrivee()));
        } else {
            // valeurs par défaut : aujourd’hui + maintenant arrondi
            spDateDepart.setValue(new Date());
            spDateArrivee.setValue(new Date());
            spHeureDepart.setValue(new Date());
            spHeureArrivee.setValue(new Date());
            chkReservable.setSelected(true);
        }

        JButton btnSave = new JButton("💾 Enregistrer");
        btnSave.addActionListener(e -> {
            Aeroport dep = (Aeroport) cbDepart.getSelectedItem();
            Aeroport arr = (Aeroport) cbArrivee.getSelectedItem();

            if (dep == null || arr == null) {
                JOptionPane.showMessageDialog(this, "Choisis les aéroports.");
                return;
            }
            if (dep.getId() == arr.getId()) {
                JOptionPane.showMessageDialog(this, "Départ et arrivée doivent être différents.");
                return;
            }

            LocalDate dateDep = toLocalDate(spDateDepart.getValue());
            LocalDate dateArr = toLocalDate(spDateArrivee.getValue());
            LocalTime heureDep = toLocalTime(spHeureDepart.getValue());
            LocalTime heureArr = toLocalTime(spHeureArrivee.getValue());

            // cohérence date/heure
            if (dateArr.isBefore(dateDep)) {
                JOptionPane.showMessageDialog(this, "La date d'arrivée ne peut pas être avant la date de départ.");
                return;
            }
            if (dateArr.equals(dateDep) && heureArr.isBefore(heureDep)) {
                JOptionPane.showMessageDialog(this, "L'heure d'arrivée ne peut pas être avant l'heure de départ (même jour).");
                return;
            }

            Vol v = (existing == null) ? new Vol() : existing;
            v.setDepart(dep);
            v.setArrivee(arr);
            v.setDateDepart(dateDep);
            v.setDateArrivee(dateArr);
            v.setHeureDepart(heureDep);
            v.setHeureArrivee(heureArr);
            v.setReservable(chkReservable.isSelected());

            if (existing == null) volDAO.insert(v);
            else volDAO.update(v);

            parent.refresh();
            dispose();
        });

        JButton btnCancel = new JButton("Annuler");
        btnCancel.addActionListener(e -> dispose());

        // --- Layout (propre) ---
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        int y = 0;
        addRow(form, c, y++, "Aéroport départ", cbDepart);
        addRow(form, c, y++, "Aéroport arrivée", cbArrivee);
        addRow(form, c, y++, "Date départ", spDateDepart);
        addRow(form, c, y++, "Heure départ", spHeureDepart);
        addRow(form, c, y++, "Date arrivée", spDateArrivee);
        addRow(form, c, y++, "Heure arrivée", spHeureArrivee);

        c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        form.add(chkReservable, c);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actions.add(btnCancel);
        actions.add(btnSave);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private void addRow(JPanel p, GridBagConstraints c, int y, String label, JComponent field) {
        c.gridy = y;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 0;
        p.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1;
        p.add(field, c);
    }

    private void selectAeroport(JComboBox<Aeroport> cb, Aeroport target) {
        if (target == null) return;
        for (int i = 0; i < cb.getItemCount(); i++) {
            Aeroport a = cb.getItemAt(i);
            if (a != null && a.getId() == target.getId()) {
                cb.setSelectedIndex(i);
                return;
            }
        }
    }

    private LocalDate toLocalDate(Object value) {
        Date d = (Date) value;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime toLocalTime(Object value) {
        Date d = (Date) value;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
    }

    private Date toDate(LocalDate ld) {
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date toDate(LocalTime lt) {
        // date “dummy” pour spinner heure
        return Date.from(lt.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
}
