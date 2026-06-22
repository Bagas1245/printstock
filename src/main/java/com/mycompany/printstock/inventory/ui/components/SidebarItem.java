package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SidebarItem extends JPanel {
    private boolean active = false;
    private boolean hover = false;
    private final JLabel iconLabel;
    private final JLabel textLabel;

    public SidebarItem(Icon icon, String text) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(400, 48));
        setPreferredSize(new Dimension(240, 44));

        iconLabel = new JLabel(icon);
        iconLabel.setForeground(new Color(100, 116, 139));

        textLabel = new JLabel(text);
        textLabel.setFont(new Font("Inter", Font.BOLD, 13));
        textLabel.setForeground(new Color(71, 85, 105));

        add(iconLabel);
        add(textLabel);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hover = true;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hover = false;
                repaint();
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            textLabel.setForeground(Color.WHITE);
            iconLabel.setForeground(Color.WHITE);
        } else {
            textLabel.setForeground(new Color(71, 85, 105));
            iconLabel.setForeground(new Color(100, 116, 139));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (active) {
            g2.setColor(new Color(15, 23, 42));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
        } else if (hover) {
            g2.setColor(new Color(241, 245, 249));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
