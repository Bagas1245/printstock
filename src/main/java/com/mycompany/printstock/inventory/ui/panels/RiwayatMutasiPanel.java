package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.dao.LogStokDAO;
import com.mycompany.printstock.inventory.model.LogStok;
import com.mycompany.printstock.inventory.ui.components.GlassPanel;
import com.mycompany.printstock.inventory.ui.components.LucideIcon;
import com.mycompany.printstock.inventory.ui.components.ModernButton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        ModernButton btnExport = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.FILE_TEXT, 16, Color.WHITE), "Export Excel");
        btnExport.setBackground(new Color(16, 185, 129)); 
        btnExport.addActionListener(e -> exportToExcel());
        
        ModernButton btnRefresh = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.CALENDAR, 16, Color.WHITE), "Refresh Data");
        btnRefresh.addActionListener(e -> refreshData());

        buttonPanel.add(btnExport);
        buttonPanel.add(btnRefresh);

        top.add(lblTitle, BorderLayout.WEST);
        top.add(buttonPanel, BorderLayout.EAST);

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

    private void exportToExcel() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk diexport!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan File Export Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("File Excel (*.xlsx)", "xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Riwayat Mutasi");

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));
                }

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }
                
                JOptionPane.showMessageDialog(this, "Data berhasil diexport ke:\n" + filePath, "Export Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mengexport data Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
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
