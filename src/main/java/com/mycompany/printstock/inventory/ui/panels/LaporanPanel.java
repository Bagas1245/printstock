package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.StokKeluar;
import com.mycompany.printstock.inventory.model.StokMasuk;
import com.mycompany.printstock.inventory.service.DashboardService;
import com.mycompany.printstock.inventory.ui.components.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaporanPanel extends JPanel {
    private final DashboardService service;
    private JPanel chartPanel;
    private JPanel detailPanel;
    private JLabel totalLabel;
    
    private LocalDate startDate = LocalDate.now().minusDays(7);
    private LocalDate endDate = LocalDate.now();

    public LaporanPanel() {
        this.service = new DashboardService();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        initComponents();
        refreshData();
    }

    private void initComponents() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        JLabel lblMulai = new JLabel("Dari:");
        lblMulai.setFont(new Font("Inter", Font.PLAIN, 12));
        JTextField txtMulai = new JTextField(startDate.toString(), 10);
        txtMulai.setToolTipText("Format: YYYY-MM-DD");

        JLabel lblSelesai = new JLabel("Sampai:");
        lblSelesai.setFont(new Font("Inter", Font.PLAIN, 12));
        JTextField txtSelesai = new JTextField(endDate.toString(), 10);
        txtSelesai.setToolTipText("Format: YYYY-MM-DD");

        ModernButton btnTerapkan = new ModernButton("Terapkan");
        btnTerapkan.setRadius(8);
        btnTerapkan.setBgColor(new Color(15, 23, 42));
        btnTerapkan.setForeground(Color.WHITE);

        btnTerapkan.addActionListener(e -> {
            try {
                LocalDate parsedStart = LocalDate.parse(txtMulai.getText());
                LocalDate parsedEnd = LocalDate.parse(txtSelesai.getText());
                
                if (parsedStart.isAfter(parsedEnd)) {
                    JOptionPane.showMessageDialog(this, "Tanggal mulai tidak boleh lebih besar dari tanggal selesai!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                startDate = parsedStart;
                endDate = parsedEnd;
                refreshData();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format tanggal salah! Harap gunakan format YYYY-MM-DD (Contoh: 2026-06-12)", "Error Format", JOptionPane.ERROR_MESSAGE);
            }
        });

        filterPanel.add(lblMulai);
        filterPanel.add(txtMulai);
        filterPanel.add(lblSelesai);
        filterPanel.add(txtSelesai);
        filterPanel.add(btnTerapkan);

        totalLabel = new JLabel("Total Aktivitas: 0 unit");
        totalLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        totalLabel.setForeground(new Color(100, 116, 139));

        top.add(filterPanel, BorderLayout.WEST);
        top.add(totalLabel, BorderLayout.EAST);

        JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));
        content.setOpaque(false);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);
        GlassPanel chartGlass = new GlassPanel(new BorderLayout());
        chartGlass.setRadius(16);
        chartGlass.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel chartTitle = new JLabel("Grafik Aktivitas Stok");
        chartTitle.setFont(new Font("Inter", Font.BOLD, 14));
        chartTitle.setForeground(new Color(15, 23, 42));
        chartGlass.add(chartTitle, BorderLayout.NORTH);
        chartGlass.add(chartPanel, BorderLayout.CENTER);
        content.add(chartGlass);

        detailPanel = new JPanel();
        detailPanel.setOpaque(false);
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        GlassPanel detailGlass = new GlassPanel(new BorderLayout());
        detailGlass.setRadius(16);
        detailGlass.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel detailTitle = new JLabel("Detail Riwayat Stok");
        detailTitle.setFont(new Font("Inter", Font.BOLD, 14));
        detailTitle.setForeground(new Color(15, 23, 42));
        detailGlass.add(detailTitle, BorderLayout.NORTH);
        JScrollPane detailScroll = new JScrollPane(detailPanel);
        detailScroll.setOpaque(false);
        detailScroll.getViewport().setOpaque(false);
        detailScroll.setBorder(null);
        detailGlass.add(detailScroll, BorderLayout.CENTER);
        content.add(detailGlass);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(top);
        wrapper.add(Box.createVerticalStrut(20));
        wrapper.add(content);

        add(wrapper, BorderLayout.CENTER);
    }

    public void refreshData() {
        try {
            List<StokMasuk> listMasuk = service.getStokMasukByDateRange(startDate, endDate);
            List<StokKeluar> listKeluar = service.getStokKeluarByDateRange(startDate, endDate);

            Map<String, int[]> aggregatedStats = new HashMap<>(); 
            int totalAktivitas = 0;

            class Transaction {
                Barang barang; String type; int qty; String date;
                Transaction(Barang b, String t, int q, String d) {
                    this.barang = b; this.type = t; this.qty = q; this.date = d;
                }
            }
            List<Transaction> transactions = new ArrayList<>();

            for (StokMasuk sm : listMasuk) {
                String namaBarang = sm.getBarang().getNama();
                aggregatedStats.putIfAbsent(namaBarang, new int[]{0, 0});
                aggregatedStats.get(namaBarang)[0] += sm.getJumlah();
                totalAktivitas += sm.getJumlah();
                transactions.add(new Transaction(sm.getBarang(), "Masuk", sm.getJumlah(), sm.getTanggal()));
            }

            for (StokKeluar sk : listKeluar) {
                String namaBarang = sk.getBarang().getNama();
                aggregatedStats.putIfAbsent(namaBarang, new int[]{0, 0});
                aggregatedStats.get(namaBarang)[1] += sk.getJumlah();
                totalAktivitas += sk.getJumlah();
                transactions.add(new Transaction(sk.getBarang(), "Keluar", sk.getJumlah(), sk.getTanggal()));
            }

            totalLabel.setText("Total Aktivitas: " + String.format("%,d", totalAktivitas) + " unit");

            chartPanel.removeAll();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, int[]> entry : aggregatedStats.entrySet()) {
                dataset.addValue(entry.getValue()[0], "Stok Masuk", entry.getKey());
                dataset.addValue(entry.getValue()[1], "Stok Keluar", entry.getKey());
            }

            if (!aggregatedStats.isEmpty()) {
                JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                chart.setBackgroundPaint(new Color(0, 0, 0, 0));
                CategoryPlot plot = chart.getCategoryPlot();
                plot.setBackgroundPaint(new Color(0, 0, 0, 0));
                plot.setOutlineVisible(false);
                plot.setRangeGridlinePaint(new Color(241, 245, 249));
                plot.setDomainGridlinesVisible(false);

                BarRenderer renderer = (BarRenderer) plot.getRenderer();
                renderer.setSeriesPaint(0, new Color(16, 185, 129));
                renderer.setSeriesPaint(1, new Color(239, 68, 68));
                renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
                renderer.setShadowVisible(false);
                renderer.setItemMargin(0.1);

                plot.getDomainAxis().setTickLabelFont(new Font("Inter", Font.PLAIN, 11));
                plot.getRangeAxis().setTickLabelFont(new Font("Inter", Font.PLAIN, 11));
                plot.getDomainAxis().setAxisLineVisible(false);
                plot.getRangeAxis().setAxisLineVisible(false);
                plot.setOrientation(org.jfree.chart.plot.PlotOrientation.HORIZONTAL);

                ChartPanel cp = new ChartPanel(chart);
                cp.setOpaque(false);
                chartPanel.add(cp, BorderLayout.CENTER);
            } else {
                JLabel empty = new JLabel("Tidak ada data aktivitas di rentang tanggal ini", SwingConstants.CENTER);
                empty.setFont(new Font("Inter", Font.PLAIN, 13));
                empty.setForeground(new Color(148, 163, 184));
                chartPanel.add(empty, BorderLayout.CENTER);
            }

            detailPanel.removeAll();
            if (transactions.isEmpty()) {
                JLabel empty = new JLabel("Tidak ada data", SwingConstants.CENTER);
                empty.setFont(new Font("Inter", Font.PLAIN, 13));
                empty.setForeground(new Color(148, 163, 184));
                detailPanel.add(empty);
            } else {
                transactions.sort((a, b) -> b.date.compareTo(a.date));
                for (Transaction t : transactions) {
                    detailPanel.add(createDetailItem(t.barang, t.qty, t.type, t.date));
                    detailPanel.add(Box.createVerticalStrut(8));
                }
            }

            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createDetailItem(Barang b, int qty, String type, String date) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBackground(new Color(248, 250, 252));
        p.setOpaque(true);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 4));
        left.setOpaque(false);
        JLabel name = new JLabel(b.getNama());
        name.setFont(new Font("Inter", Font.BOLD, 12));
        name.setForeground(new Color(15, 23, 42));
        
        JLabel catAndDate = new JLabel(b.getKategori() + "  •  " + date);
        catAndDate.setFont(new Font("Inter", Font.PLAIN, 11));
        catAndDate.setForeground(new Color(100, 116, 139));
        
        left.add(name);
        left.add(catAndDate);

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 2));
        right.setOpaque(false);

        boolean isMasuk = type.equals("Masuk");
        String prefix = isMasuk ? "+" : "-";
        Color amtColor = isMasuk ? new Color(16, 185, 129) : new Color(239, 68, 68);

        JLabel amt = new JLabel(prefix + qty + " " + b.getSatuan(), SwingConstants.RIGHT);
        amt.setFont(new Font("Inter", Font.BOLD, 13));
        amt.setForeground(amtColor);

        JLabel lblType = new JLabel(type, SwingConstants.RIGHT);
        lblType.setFont(new Font("Inter", Font.PLAIN, 10));
        lblType.setForeground(amtColor);

        right.add(amt);
        right.add(lblType);

        p.add(left, BorderLayout.CENTER);
        p.add(right, BorderLayout.EAST);
        return p;
    }
}