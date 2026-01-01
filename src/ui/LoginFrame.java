/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import service.AuthService;
import javax.swing.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Gestion des Vols - Login");
        setSize(400,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(null);

        JLabel u = new JLabel("Username");
        u.setBounds(50,50,100,30);
        JTextField tu = new JTextField();
        tu.setBounds(150,50,180,30);

        JLabel pw = new JLabel("Password");
        pw.setBounds(50,100,100,30);
        JPasswordField tp = new JPasswordField();
        tp.setBounds(150,100,180,30);

        JButton b = new JButton("Login");
        b.setBounds(150,150,100,30);

        b.addActionListener(e -> {
            if (new AuthService().login(tu.getText(), new String(tp.getPassword()))) {
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur d'authentification");
            }
        });

        p.add(u); p.add(tu); p.add(pw); p.add(tp); p.add(b);
        add(p);
    }
}

