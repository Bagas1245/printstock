package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernTextField extends JTextField {
    private int radius = 10;
    private Icon prefixIcon;
    private Color borderColor = new Color(226, 232, 240);
    private Color focusColor = new Color(15, 23, 42);
    private boolean focused = false;

    public ModernTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(new EmptyBorder(10, 12, 10, 12));
        setFont(new Font("Inter", Font.PLAIN, 13));
        setBackground(Color.WHITE);
        setForeground(new Color(51, 65, 85));

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                focused = true;
                repaint();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                focused = false;
                repaint();
            }
        });
    }

    public ModernTextField() {
        this(20);
    }

    public void setPrefixIcon(Icon icon) {
        this.prefixIcon = icon;
        setBorder(new EmptyBorder(10, 36, 10, 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));

        g2.setColor(focused ? focusColor : borderColor);
        g2.setStroke(new BasicStroke(focused ? 2f : 1f));
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));

        g2.dispose();

        if (prefixIcon != null) {
            int iconY = (getHeight() - prefixIcon.getIconHeight()) / 2;
            prefixIcon.paintIcon(this, g, 10, iconY);
        }

        super.paintComponent(g);
    }
}
