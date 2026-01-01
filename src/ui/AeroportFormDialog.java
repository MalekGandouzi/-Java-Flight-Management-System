/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.Aeroport;
import model.Pays;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class AeroportFormDialog extends JDialog {

    public AeroportFormDialog(
            Window owner,
            Aeroport existing,
            List<Pays> paysList,
            Consumer<Aeroport> onSave
    ) {
        super(owner, existing == null ? "Ajouter Aéroport" : "Modifier Aéroport", ModalityType.APPLICATION_MODAL);

        JTextField tfNom = new JTextField(20);

        JComboBox<Pays> cbPays = new JComboBox<>(paysList.toArray(new Pays[0]));
        cbPays.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getNom())
        );

        if (existing != null) {
            tfNom.setText(existing.getNom());
            if (existing.getPays() != null) {
                for (int i = 0; i < cbPays.getItemCount(); i++) {
                    Pays p = cbPays.getItemAt(i);
                    if (p.getId() == existing.getPays().getId()) {
                        cbPays.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        JButton btnSave = new JButton("💾 Enregistrer");
        btnSave.addActionListener(e -> {
            String nom = tfNom.getText().trim();
            Pays pays = (Pays) cbPays.getSelectedItem();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nom obligatoire");
                return;
            }
            if (pays == null) {
                JOptionPane.showMessageDialog(this, "Choisis un pays");
                return;
            }

            Aeroport a = (existing == null) ? new Aeroport() : existing;
            a.setNom(nom);
            a.setPays(pays);

            onSave.accept(a);
            dispose();
        });

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        form.add(new JLabel("Nom de l'aéroport"));
        form.add(tfNom);
        form.add(new JLabel("Pays"));
        form.add(cbPays);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnSave);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}
