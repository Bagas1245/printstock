package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {
    private int radius = 10;
    private Color bgColor = new Color(15, 23, 42);
    private Color hoverColor = new Color(30, 41, 59);
    private Color pressColor = new Color(51, 65, 85);
    private boolean isGhost = false;

    public ModernButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Inter", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
    }

    public ModernButton(Icon icon, String text) {
        this(text);
        setIcon(icon);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setIconTextGap(8);
    }

    public void setRadius(int r) { this.radius = r; }
    public void setBgColor(Color c) { this.bgColor = c; this.hoverColor = c.brighter(); }
    public void setGhost(boolean g) { 
        this.isGhost = g; 
        if (g) {
            setForeground(new Color(71, 85, 105));
            setBackground(new Color(241, 245, 249));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color current = bgColor;
        if (getModel().isPressed()) current = pressColor;
        else if (getModel().isRollover()) current = hoverColor;

        if (isGhost && !getModel().isRollover() && !getModel().isPressed()) {
            g2.setColor(new Color(241, 245, 249));
        } else {
            g2.setColor(current);
        }

        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));

        if (isGhost && (getModel().isRollover() || getModel().isPressed())) {
            g2.setColor(new Color(226, 232, 240));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
