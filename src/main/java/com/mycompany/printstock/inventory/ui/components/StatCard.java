package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import java.awt.*;

public class StatCard extends GlassPanel {
    private final JLabel valueLabel;
    private final JLabel titleLabel;
    private final JPanel iconPanel;

    public StatCard(String title, String value, LucideIcon.IconName iconName, Color iconBg) {
        super(new BorderLayout());
        setRadius(16);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 4));
        left.setOpaque(false);

        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 116, 139));

        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 28));
        valueLabel.setForeground(new Color(15, 23, 42));

        left.add(titleLabel);
        left.add(valueLabel);

        iconPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(48, 48));
        iconPanel.add(new JLabel(LucideIcon.createIcon(iconName, 22, Color.WHITE)));

        add(left, BorderLayout.CENTER);
        add(iconPanel, BorderLayout.EAST);
    }

    public void setAlert(boolean alert) {
        valueLabel.setForeground(alert ? new Color(220, 38, 38) : new Color(15, 23, 42));
    }
}
