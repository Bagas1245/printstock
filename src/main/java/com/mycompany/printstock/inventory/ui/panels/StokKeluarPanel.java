package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.dao.PengajuanStokDAO;
import com.mycompany.printstock.inventory.model.PengajuanStok;
import com.mycompany.printstock.inventory.ui.components.*;
import com.mycompany.printstock.inventory.ui.dialogs.StokKeluarDialog;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StokKeluarPanel extends JPanel {
    private final PengajuanStokDAO pengajuanDAO;
    private DefaultTableModel model;
    private JTable table;

    public StokKeluarPanel() {
        this.pengajuanDAO = new PengajuanStokDAO();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        initComponents();
        refreshData();
    }

    private void initComponents() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        
        ModernButton btn = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.MINUS, 16, Color.WHITE), "Input Stok Keluar");
        btn.setBgColor(new Color(239, 68, 68));
        
        btn.addActionListener(e -> {
            StokKeluarDialog dialog = new StokKeluarDialog(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    PengajuanStok p = new PengajuanStok();
                    p.setBarangId(dialog.getBarangId());
                    
                    p.setPetugasId(2); 
                    
                    p.setJenisMutasi("KELUAR");
                    p.setJumlah(dialog.getJumlah());
                    p.setTanggal(new java.sql.Date(System.currentTimeMillis())); 
                    p.setKeterangan(dialog.getKeterangan());
                    
                    pengajuanDAO.insert(p);
                    
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Pengajuan stok keluar dikirim (PENDING)", ToastNotification.Type.SUCCESS);
                    refreshData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        top.add(btn);

        String[] cols = {"Tanggal", "Barang", "Status", "Jumlah", "Keterangan"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        GlassPanel panel = new GlassPanel(new BorderLayout());
        panel.setRadius(16);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    public void refreshData() {
        try {
            List<PengajuanStok> list = pengajuanDAO.findAllPending();
            model.setRowCount(0);
            for (PengajuanStok p : list) {
                if ("KELUAR".equals(p.getJenisMutasi())) {
                    model.addRow(new Object[]{
                        p.getTanggal(), 
                        p.getNamaBarang(), 
                        "⏳ " + p.getStatus(), 
                        "-" + p.getJumlah(),
                        p.getKeterangan()
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void styleTable(JTable t) {
        t.setOpaque(false);
        t.setRowHeight(48);
        t.setFont(new Font("Inter", Font.PLAIN, 13));
        t.setForeground(new Color(51, 65, 85));
        t.setSelectionBackground(new Color(241, 245, 249));
        t.setSelectionForeground(new Color(51, 65, 85));
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        t.getTableHeader().setForeground(new Color(100, 116, 139));
        t.getTableHeader().setBackground(new Color(248, 250, 252));
        t.getTableHeader().setPreferredSize(new Dimension(0, 44));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
    }
}