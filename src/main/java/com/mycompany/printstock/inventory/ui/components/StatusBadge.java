package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatusBadge extends JLabel {
    public enum Type { LOW, HIGH, NORMAL }
    private Type type;

    public StatusBadge(String text, Type type) {
        super(text);
        this.type = type;
        setOpaque(false);
        setFont(new Font("Inter", Font.BOLD, 11));
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        setHorizontalAlignment(SwingConstants.CENTER);
        updateColor();
    }

    private void updateColor() {
        switch (type) {
            case LOW:
                setForeground(new Color(180, 83, 9));
                setBackground(new Color(255, 251, 235));
                break;
            case HIGH:
                setForeground(new Color(6, 95, 70));
                setBackground(new Color(236, 253, 245));
                break;
            case NORMAL:
                setForeground(new Color(71, 85, 105));
                setBackground(new Color(241, 245, 249));
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
        g2.dispose();
        super.paintComponent(g);
    }
}
