package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.dao.LogStokDAO;
import com.mycompany.printstock.inventory.model.LogStok;
import com.mycompany.printstock.inventory.ui.components.GlassPanel;
import com.mycompany.printstock.inventory.ui.components.LucideIcon;
import com.mycompany.printstock.inventory.ui.components.ModernButton;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RiwayatMutasiPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private LogStokDAO logDAO;

    public RiwayatMutasiPanel() {
        this.logDAO = new LogStokDAO();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        initComponents();
        refreshData();
    }

    private void initComponents() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Riwayat Mutasi Stok");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitle.setForeground(new Color(15, 23, 42));
        
        ModernButton btnRefresh = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.CALENDAR, 16, Color.WHITE), "Refresh Data");
        btnRefresh.addActionListener(e -> refreshData());

        top.add(lblTitle, BorderLayout.WEST);
        top.add(btnRefresh, BorderLayout.EAST);

        String[] columns = {"Tanggal Persetujuan", "Nama Barang", "Jenis Mutasi", "Jumlah", "Disetujui Oleh"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        GlassPanel panel = new GlassPanel(new BorderLayout(0, 20));
        panel.setRadius(16);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    public void refreshData() {
        try {
            List<LogStok> logs = logDAO.findAllLogs();
            tableModel.setRowCount(0);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");

            for (LogStok log : logs) {
                String prefix = log.getJenisMutasi().equals("MASUK") ? "+" : "-";
                
                Object[] row = {
                    sdf.format(log.getWaktuDisetujui()),
                    log.getNamaBarang(),
                    log.getJenisMutasi(),
                    prefix + log.getJumlah(),
                    log.getNamaAdmin()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat mutasi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTable(JTable t) {
        t.setOpaque(false);
        t.setRowHeight(40);
        t.setFont(new Font("Inter", Font.PLAIN, 14));
        t.setForeground(new Color(51, 65, 85));
        t.setSelectionBackground(new Color(241, 245, 249));
        t.setSelectionForeground(new Color(15, 23, 42));
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        
        t.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        t.getTableHeader().setForeground(new Color(100, 116, 139));
        t.getTableHeader().setBackground(new Color(248, 250, 252));
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String mutasi = (String) table.getValueAt(row, 2);
                
                if (!isSelected) {
                    if (mutasi.equals("MASUK")) {
                        c.setForeground(new Color(16, 185, 129));
                    } else {
                        c.setForeground(new Color(239, 68, 68));
                    }
                }
                return c;
            }
        };
        
        t.getColumnModel().getColumn(2).setCellRenderer(customRenderer);
        t.getColumnModel().getColumn(3).setCellRenderer(customRenderer);
    }
}