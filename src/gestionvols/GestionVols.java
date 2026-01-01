/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestionvols;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import ui.LoginFrame;

public class GestionVols {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            // Personnalisation globale
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Table.rowHeight", 28);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        new LoginFrame().setVisible(true);
    }
}


