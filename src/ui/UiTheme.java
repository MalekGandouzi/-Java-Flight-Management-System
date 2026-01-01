/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UiTheme {

    // 🎨 Couleurs
    public static final Color SIDEBAR = new Color(30, 41, 59);
    public static final Color SIDEBAR_HOVER = new Color(51, 65, 85);
    public static final Color BG = new Color(241, 245, 249);
    public static final Color CARD = Color.WHITE;
    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color DANGER = new Color(220, 38, 38);
    public static final Color SUCCESS = new Color(22, 163, 74);
    public static final Color TEXT_DARK = new Color(15, 23, 42);

    // 🧩 Page principale
    public static JPanel page() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        return p;
    }

    // 📦 Card
    public static JPanel card(Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(new EmptyBorder(14, 14, 14, 14));
        p.add(c);
        return p;
    }

    // 🔘 Bouton stylé
    public static JButton button(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
        return b;
    }

    // 🧭 Bouton sidebar
    public static JButton sidebarButton(String text, String icon) {
        JButton b = new JButton(icon + "  " + text);
        b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16)); // 🔴 icônes visibles
        b.setForeground(Color.WHITE);
        b.setBackground(SIDEBAR);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 16, 12, 16));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(SIDEBAR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(SIDEBAR);
            }
        });

        return b;
    }
    public static JButton cardButton(String text, Color bg) {
    JButton b = new JButton(text);
    b.setFocusPainted(false);
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
    b.setBorder(new javax.swing.border.EmptyBorder(8, 14, 8, 14));
    return b;
}

}
