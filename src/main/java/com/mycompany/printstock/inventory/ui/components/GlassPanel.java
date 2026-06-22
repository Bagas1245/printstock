package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GlassPanel extends JPanel {
    private int radius = 16;
    private Color borderColor = new Color(226, 232, 240);
    private float borderWidth = 1f;
    private boolean shadow = false;

    public GlassPanel() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    public GlassPanel(LayoutManager layout) {
        this();
        setLayout(layout);
    }

    public void setRadius(int radius) { this.radius = radius; repaint(); }
    public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
    public void setShadow(boolean s) { this.shadow = s; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (shadow) {
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fill(new RoundRectangle2D.Float(2, 4, w - 4, h - 4, radius, radius));
        }

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radius, radius));

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderWidth));
        g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radius, radius));

        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius).contains(x, y);
    }
}
