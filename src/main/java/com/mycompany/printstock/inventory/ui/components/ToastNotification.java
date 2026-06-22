package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ToastNotification {
    private static JWindow currentToast;

    public static void show(JFrame parent, String message, Type type) {
        if (currentToast != null) {
            currentToast.dispose();
        }

        JWindow toast = new JWindow(parent);
        currentToast = toast;

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.setColor(type == Type.SUCCESS ? new Color(16, 185, 129) : new Color(100, 116, 139));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        label.setFont(new Font("Inter", Font.BOLD, 13));
        label.setForeground(type == Type.SUCCESS ? new Color(6, 95, 70) : new Color(51, 65, 85));
        panel.add(label, BorderLayout.CENTER);

        toast.add(panel);
        toast.setSize(320, 50);

        Point loc = parent.getLocationOnScreen();
        toast.setLocation(loc.x + parent.getWidth() - 340, loc.y + parent.getHeight() - 80);

        toast.setVisible(true);

        new Timer(3000, e -> {
            toast.dispose();
            if (currentToast == toast) currentToast = null;
        }).start();
    }

    public enum Type { SUCCESS, INFO }
}
