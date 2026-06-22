package com.mycompany.printstock.inventory;

import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.printstock.inventory.ui.main.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 13));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}