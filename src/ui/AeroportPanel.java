package ui;

import dao.AeroportDAO;
import dao.PaysDAO;
import model.Aeroport;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AeroportPanel extends JPanel {

    private final AeroportDAO aeroportDAO = new AeroportDAO();
    private final PaysDAO paysDAO = new PaysDAO();

    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 16, 16));

    public AeroportPanel() {
        setLayout(new BorderLayout(10, 10));

        // ✅ fond global propre
        JPanel page = UiTheme.page();

        JLabel title = new JLabel("Aéroports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UiTheme.TEXT_DARK);

        JButton add = UiTheme.button("➕ Ajouter Aéroport", UiTheme.PRIMARY);
        add.addActionListener(e -> openForm(null));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(add, BorderLayout.EAST);

        cardsPanel.setOpaque(false);

        JScrollPane sp = new JScrollPane(cardsPanel);
        sp.setBorder(null);
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);

        page.add(top, BorderLayout.NORTH);
        page.add(sp, BorderLayout.CENTER);

        add(page, BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        cardsPanel.removeAll();
        for (Aeroport a : aeroportDAO.findAll()) {
            cardsPanel.add(createCard(a));
        }
        revalidate();
        repaint();
    }

    private JPanel createCard(Aeroport a) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UiTheme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)), // gris doux
                new EmptyBorder(14, 14, 14, 14)
        ));

        // ✅ Top actions (boutons grands)
        JButton edit = UiTheme.cardButton("✏️ Modifier", UiTheme.SUCCESS);
        edit.addActionListener(e -> openForm(a));
        addHover(edit, UiTheme.SUCCESS);

        JButton del = UiTheme.cardButton("🗑️ Supprimer", UiTheme.DANGER);
        del.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                    this,
                    "Supprimer l'aéroport : " + a.getNom() + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (ok == JOptionPane.YES_OPTION) {
                aeroportDAO.delete(a.getId());
                refresh();
            }
        });
        addHover(del, UiTheme.DANGER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(edit);
        actions.add(del);

        // ✅ Center (icone + nom + pays)
        JLabel icon = new JLabel("🛫", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));

        JLabel name = new JLabel(a.getNom(), SwingConstants.CENTER);
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(UiTheme.TEXT_DARK);

        String paysNom = (a.getPays() == null) ? "-" : a.getPays().getNom();
        JLabel pays = new JLabel("🌍 " + paysNom, SwingConstants.CENTER);
        pays.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        pays.setForeground(new Color(100, 116, 139));

        JPanel center = new JPanel(new GridLayout(0, 1, 6, 6));
        center.setOpaque(false);
        center.add(icon);
        center.add(name);
        center.add(pays);

        card.add(actions, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);

        return card;
    }

    private void addHover(JButton b, Color base) {
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(base.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(base);
            }
        });
    }

    private void openForm(Aeroport existing) {
        AeroportFormDialog dlg = new AeroportFormDialog(
                SwingUtilities.getWindowAncestor(this),
                existing,
                paysDAO.findAll(),
                saved -> {
                    if (existing == null) aeroportDAO.insert(saved);
                    else aeroportDAO.update(saved);

                    refresh();
                }
        );
        dlg.setVisible(true);
    }
}
