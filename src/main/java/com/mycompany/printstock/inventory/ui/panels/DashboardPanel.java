package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.StokMasuk;
import com.mycompany.printstock.inventory.model.StokKeluar;
import com.mycompany.printstock.inventory.service.DashboardService;
import com.mycompany.printstock.inventory.ui.components.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private final DashboardService service;
    private JPanel statsGrid;
    private JPanel chartPanel;
    private JPanel alertsPanel;
    private JPanel activityPanel;

    public DashboardPanel() {
        this.service = new DashboardService();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        initComponents();
        refreshData();
    }

    private void initComponents() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        statsGrid = new JPanel(new GridLayout(1, 4, 16, 16));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsGrid.setPreferredSize(new Dimension(800, 110));
        content.add(statsGrid);
        content.add(Box.createVerticalStrut(24));

        JPanel bottom = new JPanel(new GridLayout(1, 2, 24, 0));
        bottom.setOpaque(false);
        bottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);
        GlassPanel chartGlass = new GlassPanel(new BorderLayout());
        chartGlass.setRadius(16);
        chartGlass.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        chartGlass.add(chartPanel, BorderLayout.CENTER);
        bottom.add(chartGlass);

        JPanel rightCol = new JPanel();
        rightCol.setOpaque(false);
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));

        alertsPanel = new JPanel();
        alertsPanel.setOpaque(false);
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));

        JScrollPane alertsScroll = new JScrollPane(alertsPanel);
        alertsScroll.setOpaque(false);
        alertsScroll.getViewport().setOpaque(false);
        alertsScroll.setBorder(BorderFactory.createEmptyBorder());
        alertsScroll.getVerticalScrollBar().setUnitIncrement(16);
        alertsScroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0)); 

        GlassPanel alertsGlass = new GlassPanel(new BorderLayout(0, 10));
        alertsGlass.setRadius(16);
        alertsGlass.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel alertsTitle = new JLabel("Peringatan Stok");
        alertsTitle.setFont(new Font("Inter", Font.BOLD, 14));
        alertsTitle.setForeground(new Color(15, 23, 42));
        alertsGlass.add(alertsTitle, BorderLayout.NORTH);
        alertsGlass.add(alertsScroll, BorderLayout.CENTER); 
        rightCol.add(alertsGlass);

        rightCol.add(Box.createVerticalStrut(16));

        activityPanel = new JPanel();
        activityPanel.setOpaque(false);
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));

        JScrollPane activityScroll = new JScrollPane(activityPanel);
        activityScroll.setOpaque(false);
        activityScroll.getViewport().setOpaque(false);
        activityScroll.setBorder(BorderFactory.createEmptyBorder());
        activityScroll.getVerticalScrollBar().setUnitIncrement(16);
        activityScroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0)); 

        GlassPanel actGlass = new GlassPanel(new BorderLayout(0, 10));
        actGlass.setRadius(16);
        actGlass.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel actTitle = new JLabel("Aktivitas Terakhir");
        actTitle.setFont(new Font("Inter", Font.BOLD, 14));
        actTitle.setForeground(new Color(15, 23, 42));
        actGlass.add(actTitle, BorderLayout.NORTH);
        actGlass.add(activityScroll, BorderLayout.CENTER); 
        rightCol.add(actGlass);

        bottom.add(rightCol);
        content.add(bottom);
        add(content, BorderLayout.CENTER);
    }

    public void refreshData() {
        try {
            statsGrid.removeAll();
            int totalBarang = service.getTotalBarang();
            int totalStok = service.getTotalStok();
            List<Barang> stokRendah = service.getStokRendah();
            List<Barang> stokTinggi = service.getStokTinggi();

            StatCard card1 = new StatCard("Total Barang", String.valueOf(totalBarang), LucideIcon.IconName.PACKAGE, new Color(15, 23, 42));
            StatCard card2 = new StatCard("Total Stok", String.format("%,d", totalStok), LucideIcon.IconName.BAR_CHART3, new Color(51, 65, 85));
            StatCard card3 = new StatCard("Stok Rendah", String.valueOf(stokRendah.size()), LucideIcon.IconName.ALERT_TRIANGLE, new Color(245, 158, 11));
            card3.setAlert(stokRendah.size() > 0);
            StatCard card4 = new StatCard("Stok Tinggi", String.valueOf(stokTinggi.size()), LucideIcon.IconName.CHECK_CIRCLE2, new Color(16, 185, 129));

            statsGrid.add(card1);
            statsGrid.add(card2);
            statsGrid.add(card3);
            statsGrid.add(card4);

            chartPanel.removeAll();
            Map<String, int[]> weekly = service.getWeeklyActivity();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, int[]> entry : weekly.entrySet()) {
                dataset.addValue(entry.getValue()[0], "Stok Masuk", entry.getKey());
                dataset.addValue(entry.getValue()[1], "Stok Keluar", entry.getKey());
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Aktivitas Stok Mingguan", "", "Jumlah", dataset
            );
            chart.setBackgroundPaint(new Color(0, 0, 0, 0));
            chart.setPadding(new RectangleInsets(10, 10, 15, 10));

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(new Color(0, 0, 0, 0));
            plot.setOutlineVisible(false);
            plot.setRangeGridlinePaint(new Color(241, 245, 249));

            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(15, 23, 42));
            renderer.setSeriesPaint(1, new Color(203, 213, 225));
            renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
            renderer.setShadowVisible(false);
            renderer.setItemMargin(0.15);

            plot.getDomainAxis().setTickLabelFont(new Font("Inter", Font.PLAIN, 11));
            plot.getDomainAxis().setCategoryLabelPositions(org.jfree.chart.axis.CategoryLabelPositions.UP_45);
            
            plot.getRangeAxis().setTickLabelFont(new Font("Inter", Font.PLAIN, 11));
            plot.getDomainAxis().setAxisLineVisible(false);
            plot.getRangeAxis().setAxisLineVisible(false);

            ChartPanel cp = new ChartPanel(chart);
            cp.setOpaque(false);
            cp.setMinimumDrawWidth(0);
            cp.setMinimumDrawHeight(0);
            cp.setMaximumDrawWidth(2000);
            cp.setMaximumDrawHeight(2000);
            
            chartPanel.add(cp, BorderLayout.CENTER);

            alertsPanel.removeAll();
            if (stokRendah.isEmpty()) {
                JLabel safe = new JLabel("Semua stok dalam kondisi aman");
                safe.setFont(new Font("Inter", Font.PLAIN, 13));
                safe.setForeground(new Color(148, 163, 184));
                alertsPanel.add(safe);
            }
            for (Barang b : stokRendah) {
                alertsPanel.add(createAlertItem(b, true));
                alertsPanel.add(Box.createVerticalStrut(8));
            }

            activityPanel.removeAll();
            List<StokMasuk> masukList = service.getRecentStokMasuk(30);
            List<StokKeluar> keluarList = service.getRecentStokKeluar(30);
            java.util.List<Object> combined = new java.util.ArrayList<>();
            combined.addAll(masukList);
            combined.addAll(keluarList);
            combined.sort((a, b) -> {
                String ta = a instanceof StokMasuk ? ((StokMasuk)a).getTanggal() : ((StokKeluar)a).getTanggal();
                String tb = b instanceof StokMasuk ? ((StokMasuk)b).getTanggal() : ((StokKeluar)b).getTanggal();
                return tb.compareTo(ta);
            });
            
            int count = 0;
            for (Object o : combined) {
                if (count++ >= 20) break; 
                if (o instanceof StokMasuk) {
                    activityPanel.add(createActivityItem((StokMasuk) o));
                } else {
                    activityPanel.add(createActivityItem((StokKeluar) o));
                }
                activityPanel.add(Box.createVerticalStrut(10));
            }

            revalidate();
            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createAlertItem(Barang b, boolean isLow) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.setBackground(isLow ? new Color(255, 251, 235) : new Color(236, 253, 245));
        p.setOpaque(true);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isLow ? new Color(254, 243, 199) : new Color(209, 250, 229), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel icon = new JLabel(LucideIcon.createIcon(
            isLow ? LucideIcon.IconName.ALERT_TRIANGLE : LucideIcon.IconName.CHECK_CIRCLE2,
            18,
            isLow ? new Color(217, 119, 6) : new Color(16, 185, 129)
        ));

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        JLabel name = new JLabel(b.getNama());
        name.setFont(new Font("Inter", Font.BOLD, 12));
        name.setForeground(isLow ? new Color(120, 53, 15) : new Color(6, 78, 59));
        JLabel detail = new JLabel("Sisa " + b.getStokSaatIni() + " " + b.getSatuan() + " (Min: " + b.getStokMinimal() + ")");
        detail.setFont(new Font("Inter", Font.PLAIN, 11));
        detail.setForeground(isLow ? new Color(180, 83, 9) : new Color(5, 150, 105));
        text.add(name);
        text.add(detail);

        p.add(icon, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        return p;
    }

    private JPanel createActivityItem(StokMasuk s) {
        return createActivityRow(s.getKeterangan(), s.getTanggal(), "+" + s.getJumlah(), new Color(16, 185, 129), LucideIcon.IconName.ARROW_DOWN_LEFT);
    }

    private JPanel createActivityItem(StokKeluar s) {
        return createActivityRow(s.getKeterangan(), s.getTanggal(), "-" + s.getJumlah(), new Color(220, 38, 38), LucideIcon.IconName.ARROW_UP_RIGHT);
    }

    private JPanel createActivityRow(String desc, String date, String amount, Color color, LucideIcon.IconName icon) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel iconLbl = new JLabel(LucideIcon.createIcon(icon, 14, color));
        iconLbl.setOpaque(true);
        iconLbl.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        iconLbl.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setOpaque(false);
        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(new Font("Inter", Font.BOLD, 12));
        descLbl.setForeground(new Color(51, 65, 85));
        JLabel dateLbl = new JLabel(date);
        dateLbl.setFont(new Font("Inter", Font.PLAIN, 11));
        dateLbl.setForeground(new Color(148, 163, 184));
        center.add(descLbl);
        center.add(dateLbl);

        JLabel amtLbl = new JLabel(amount);
        amtLbl.setFont(new Font("Inter", Font.BOLD, 13));
        amtLbl.setForeground(color);

        p.add(iconLbl, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);
        p.add(amtLbl, BorderLayout.EAST);
        return p;
    }
}