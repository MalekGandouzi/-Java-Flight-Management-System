package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Gestion des Vols");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        CardLayout cl = new CardLayout();
        JPanel content = new JPanel(cl);

        content.add(new AeroportPanel(), "AEROPORTS");
        content.add(new VolPanel(), "VOLS");
        content.add(new EscalePanel(), "ESCALES");

        SidebarPanel sidebar = new SidebarPanel(cl, content, this::logout);
        sidebar.setPreferredSize(new Dimension(240, 0));

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);


        cl.show(content, "AEROPORTS");
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
