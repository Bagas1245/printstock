package com.mycompany.printstock.inventory.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;

public class LucideIcon {
    
    public enum IconName {
        LAYOUT_DASHBOARD, PACKAGE, ARROW_DOWN_LEFT, ARROW_UP_RIGHT, FILE_TEXT, 
        BELL, PLUS, SEARCH, X, ALERT_TRIANGLE, CHECK_CIRCLE2, TRENDING_UP, 
        TRENDING_DOWN, CALENDAR, EDIT2, TRASH2, CHEVRON_RIGHT, MENU, 
        BAR_CHART3, STORE, MINUS
    }
    
    public static ImageIcon createIcon(IconName name, int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(size * 0.09f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        double s = size;
        double pad = s * 0.15;
        double inner = s - 2 * pad;
        
        switch (name) {
            case LAYOUT_DASHBOARD:
                drawRect(g2, pad, pad, inner * 0.45, inner * 0.45);
                drawRect(g2, pad + inner * 0.55, pad, inner * 0.45, inner * 0.45);
                drawRect(g2, pad + inner * 0.55, pad + inner * 0.55, inner * 0.45, inner * 0.45);
                drawRect(g2, pad, pad + inner * 0.55, inner * 0.45, inner * 0.45);
                break;
                
            case PACKAGE:
                Path2D cube = new Path2D.Double();
                double cx = s/2, cy = s/2, w = inner*0.5, h = inner*0.35;
                cube.moveTo(cx - w, cy - h*0.3);
                cube.lineTo(cx, cy - h);
                cube.lineTo(cx + w, cy - h*0.3);
                cube.lineTo(cx + w, cy + h*0.7);
                cube.lineTo(cx, cy + h);
                cube.lineTo(cx - w, cy + h*0.7);
                cube.closePath();
                g2.draw(cube);
                g2.draw(new Line2D.Double(cx - w, cy - h*0.3, cx, cy + h*0.1));
                g2.draw(new Line2D.Double(cx + w, cy - h*0.3, cx, cy + h*0.1));
                g2.draw(new Line2D.Double(cx, cy + h*0.1, cx, cy + h));
                break;
                
            case ARROW_DOWN_LEFT:
                g2.draw(new Line2D.Double(s*0.7, pad, pad, s*0.7));
                g2.draw(new Line2D.Double(pad, s*0.7, pad + inner*0.3, s*0.7));
                g2.draw(new Line2D.Double(pad, s*0.7, pad, s*0.7 - inner*0.3));
                break;
                
            case ARROW_UP_RIGHT:
                g2.draw(new Line2D.Double(pad, s*0.7, s*0.7, pad));
                g2.draw(new Line2D.Double(s*0.7, pad, s*0.7 - inner*0.3, pad));
                g2.draw(new Line2D.Double(s*0.7, pad, s*0.7, pad + inner*0.3));
                break;
                
            case FILE_TEXT:
                drawRect(g2, pad, pad, inner, inner);
                g2.draw(new Line2D.Double(pad + inner*0.35, pad, pad + inner*0.35, pad + inner*0.25));
                g2.draw(new Line2D.Double(pad + inner*0.35, pad + inner*0.25, pad + inner, pad + inner*0.25));
                for (int i = 0; i < 3; i++) {
                    double y = pad + inner*0.45 + i * inner*0.18;
                    g2.draw(new Line2D.Double(pad + inner*0.15, y, pad + inner*0.85, y));
                }
                break;
                
            case BELL:
                Path2D bell = new Path2D.Double();
                bell.moveTo(pad + inner*0.2, pad + inner*0.7);
                bell.quadTo(pad + inner*0.2, pad + inner*0.2, s/2, pad + inner*0.2);
                bell.quadTo(pad + inner*0.8, pad + inner*0.2, pad + inner*0.8, pad + inner*0.7);
                bell.lineTo(pad + inner*0.85, pad + inner*0.75);
                bell.lineTo(pad + inner*0.15, pad + inner*0.75);
                bell.closePath();
                g2.draw(bell);
                g2.draw(new Line2D.Double(s*0.42, pad + inner*0.78, s*0.45, pad + inner*0.88));
                g2.draw(new Line2D.Double(s*0.58, pad + inner*0.78, s*0.55, pad + inner*0.88));
                break;
                
            case PLUS:
                g2.draw(new Line2D.Double(s/2, pad, s/2, s - pad));
                g2.draw(new Line2D.Double(pad, s/2, s - pad, s/2));
                break;
                
            case SEARCH:
                g2.draw(new Ellipse2D.Double(pad + inner*0.1, pad + inner*0.1, inner*0.55, inner*0.55));
                g2.draw(new Line2D.Double(pad + inner*0.6, pad + inner*0.6, s - pad*0.8, s - pad*0.8));
                break;
                
            case X:
                g2.draw(new Line2D.Double(pad, pad, s - pad, s - pad));
                g2.draw(new Line2D.Double(s - pad, pad, pad, s - pad));
                break;
                
            case ALERT_TRIANGLE:
                Path2D tri = new Path2D.Double();
                tri.moveTo(s/2, pad + inner*0.1);
                tri.lineTo(s - pad - inner*0.1, s - pad - inner*0.1);
                tri.lineTo(pad + inner*0.1, s - pad - inner*0.1);
                tri.closePath();
                g2.draw(tri);
                g2.draw(new Line2D.Double(s/2, pad + inner*0.35, s/2, pad + inner*0.55));
                g2.draw(new Line2D.Double(s/2, pad + inner*0.65, s/2, pad + inner*0.7));
                break;
                
            case CHECK_CIRCLE2:
                g2.draw(new Ellipse2D.Double(pad, pad, inner, inner));
                g2.draw(new Line2D.Double(pad + inner*0.28, s/2, pad + inner*0.45, pad + inner*0.65));
                g2.draw(new Line2D.Double(pad + inner*0.45, pad + inner*0.65, pad + inner*0.72, pad + inner*0.35));
                break;
                
            case TRENDING_UP:
                g2.draw(new Line2D.Double(pad, s*0.7, pad + inner*0.3, s*0.5));
                g2.draw(new Line2D.Double(pad + inner*0.3, s*0.5, pad + inner*0.55, s*0.65));
                g2.draw(new Line2D.Double(pad + inner*0.55, s*0.65, s - pad, pad + inner*0.15));
                g2.draw(new Line2D.Double(s*0.72, pad, s - pad, pad));
                g2.draw(new Line2D.Double(s - pad, pad, s - pad, pad + inner*0.28));
                break;
                
            case TRENDING_DOWN:
                g2.draw(new Line2D.Double(pad, pad + inner*0.15, pad + inner*0.3, pad + inner*0.35));
                g2.draw(new Line2D.Double(pad + inner*0.3, pad + inner*0.35, pad + inner*0.55, pad + inner*0.2));
                g2.draw(new Line2D.Double(pad + inner*0.55, pad + inner*0.2, s - pad, s*0.7));
                g2.draw(new Line2D.Double(s*0.72, s - pad, s - pad, s - pad));
                g2.draw(new Line2D.Double(s - pad, s - pad, s - pad, s*0.72));
                break;
                
            case CALENDAR:
                drawRect(g2, pad, pad + inner*0.15, inner, inner*0.85);
                g2.draw(new Line2D.Double(pad + inner*0.25, pad, pad + inner*0.25, pad + inner*0.25));
                g2.draw(new Line2D.Double(pad + inner*0.75, pad, pad + inner*0.75, pad + inner*0.25));
                g2.draw(new Line2D.Double(pad, pad + inner*0.35, s - pad, pad + inner*0.35));
                break;
                
            case EDIT2:
                g2.draw(new Line2D.Double(pad + inner*0.75, pad, s - pad, pad + inner*0.25));
                g2.draw(new Line2D.Double(pad + inner*0.55, pad + inner*0.2, pad + inner*0.15, pad + inner*0.6));
                g2.draw(new Line2D.Double(pad + inner*0.15, pad + inner*0.6, pad, s - pad));
                g2.draw(new Line2D.Double(pad, s - pad, pad + inner*0.25, pad + inner*0.75));
                break;
                
            case TRASH2:
                g2.draw(new Line2D.Double(pad + inner*0.2, pad + inner*0.25, s - pad - inner*0.2, pad + inner*0.25));
                g2.draw(new Line2D.Double(pad + inner*0.35, pad + inner*0.25, pad + inner*0.3, s - pad - inner*0.15));
                g2.draw(new Line2D.Double(s - pad - inner*0.35, pad + inner*0.25, s - pad - inner*0.3, s - pad - inner*0.15));
                g2.draw(new Line2D.Double(pad + inner*0.3, s - pad - inner*0.15, s - pad - inner*0.3, s - pad - inner*0.15));
                g2.draw(new Line2D.Double(pad + inner*0.35, pad + inner*0.25, pad + inner*0.4, pad));
                g2.draw(new Line2D.Double(s - pad - inner*0.35, pad + inner*0.25, s - pad - inner*0.4, pad));
                g2.draw(new Line2D.Double(pad + inner*0.4, pad, s - pad - inner*0.4, pad));
                g2.draw(new Line2D.Double(pad + inner*0.42, pad + inner*0.45, pad + inner*0.42, pad + inner*0.7));
                g2.draw(new Line2D.Double(s - pad - inner*0.42, pad + inner*0.45, s - pad - inner*0.42, pad + inner*0.7));
                break;
                
            case CHEVRON_RIGHT:
                g2.draw(new Line2D.Double(pad + inner*0.2, pad, s - pad - inner*0.2, s/2));
                g2.draw(new Line2D.Double(s - pad - inner*0.2, s/2, pad + inner*0.2, s - pad));
                break;
                
            case MENU:
                for (int i = 0; i < 3; i++) {
                    double y = pad + inner*0.2 + i * inner*0.3;
                    g2.draw(new Line2D.Double(pad + inner*0.1, y, s - pad - inner*0.1, y));
                }
                break;
                
            case BAR_CHART3:
                g2.draw(new Line2D.Double(pad, s - pad, s - pad, s - pad));
                g2.draw(new Line2D.Double(pad, pad, pad, s - pad));
                g2.fill(new Rectangle2D.Double(pad + inner*0.15, pad + inner*0.5, inner*0.15, inner*0.35));
                g2.fill(new Rectangle2D.Double(pad + inner*0.45, pad + inner*0.25, inner*0.15, inner*0.6));
                g2.fill(new Rectangle2D.Double(pad + inner*0.75, pad + inner*0.35, inner*0.15, inner*0.5));
                break;
                
            case STORE:
                g2.draw(new Line2D.Double(pad, pad + inner*0.15, s - pad, pad + inner*0.15));
                g2.draw(new Line2D.Double(s - pad, pad + inner*0.15, s - pad, s - pad - inner*0.15));
                g2.draw(new Line2D.Double(s - pad, s - pad - inner*0.15, pad, s - pad - inner*0.15));
                g2.draw(new Line2D.Double(pad, s - pad - inner*0.15, pad, pad + inner*0.15));
                g2.draw(new Line2D.Double(pad, pad + inner*0.45, pad + inner*0.25, pad + inner*0.3));
                g2.draw(new Line2D.Double(pad + inner*0.25, pad + inner*0.3, pad + inner*0.5, pad + inner*0.45));
                g2.draw(new Line2D.Double(pad + inner*0.5, pad + inner*0.45, pad + inner*0.75, pad + inner*0.3));
                g2.draw(new Line2D.Double(pad + inner*0.75, pad + inner*0.3, s - pad, pad + inner*0.45));
                break;
                
            case MINUS:
                g2.draw(new Line2D.Double(pad, s/2, s - pad, s/2));
                break;
        }
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    private static void drawRect(Graphics2D g2, double x, double y, double w, double h) {
        g2.draw(new RoundRectangle2D.Double(x, y, w, h, 2, 2));
    }
}