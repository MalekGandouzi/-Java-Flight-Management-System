package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarPanel extends JPanel {

    public SidebarPanel(CardLayout cl, JPanel content, Runnable onLogout) {
        setBackground(UiTheme.SIDEBAR);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(16, 12, 16, 12));

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(6, 6, 16, 6));

        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));
        avatar.setPreferredSize(new Dimension(52, 52));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(51, 65, 85));
        avatar.setForeground(Color.WHITE);
        avatar.setBorder(new EmptyBorder(6, 6, 6, 6));

        JPanel userInfo = new JPanel(new GridLayout(0, 1, 2, 2));
        userInfo.setOpaque(false);

        JLabel name = new JLabel("Malek Gandouzi");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel role = new JLabel("Admin");
        role.setForeground(new Color(203, 213, 225));
        role.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        userInfo.add(name);
        userInfo.add(role);

        header.add(avatar, BorderLayout.WEST);
        header.add(userInfo, BorderLayout.CENTER);

        
        JPanel menu = new JPanel(new GridLayout(0, 1, 0, 8));
        menu.setOpaque(false);
        menu.setBorder(new EmptyBorder(10, 0, 10, 0));

        menu.add(menuBtn("🛫", "Aéroports", cl, content, "AEROPORTS"));
        menu.add(menuBtn("✈️", "Vols", cl, content, "VOLS"));
        menu.add(menuBtn("🧭", "Escales", cl, content, "ESCALES"));

        // ---------- Footer : Déconnexion ----------
        JButton logout = UiTheme.sidebarButton("Déconnexion", "🚪");
        logout.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        logout.setBackground(new Color(185, 28, 28));         // rouge foncé
        logout.setForeground(Color.WHITE);

        logout.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                    this,
                    "Voulez-vous vous déconnecter ?",
                    "Déconnexion",
                    JOptionPane.YES_NO_OPTION
            );
            if (ok == JOptionPane.YES_OPTION) {
                if (onLogout != null) onLogout.run();
            }
        });

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));
        footer.add(logout, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(menu, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton menuBtn(String icon, String text, CardLayout cl, JPanel content, String card) {
        JButton b = UiTheme.sidebarButton(text, icon);
        b.addActionListener(e -> cl.show(content, card));
        return b;
    }
    public static JButton cardButton(String text, Color bg) {
    JButton b = new JButton(text);
    b.setFocusPainted(false);
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
    b.setBorder(new EmptyBorder(8, 14, 8, 14));           
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    return b;
}

}
