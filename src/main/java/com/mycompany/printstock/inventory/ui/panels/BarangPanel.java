package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.service.BarangService;
import com.mycompany.printstock.inventory.ui.components.*;
import com.mycompany.printstock.inventory.ui.dialogs.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BarangPanel extends JPanel {
    private final BarangService service;
    private JTable table;
    private DefaultTableModel model;
    private ModernTextField searchField;
    private List<Barang> currentData;

    public BarangPanel() {
        this.service = new BarangService();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        initComponents();
        refreshData();
    }

    private void initComponents() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        searchField = new ModernTextField(30);
        searchField.setPrefixIcon(LucideIcon.createIcon(LucideIcon.IconName.SEARCH, 18, new Color(148, 163, 184)));
        searchField.setPreferredSize(new Dimension(360, 42));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });

        ModernButton addBtn = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.PLUS, 16, Color.WHITE), "Tambah Barang");
        addBtn.addActionListener(e -> {
            AddBarangDialog dialog = new AddBarangDialog(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    service.addBarang(dialog.getBarang());
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Barang berhasil ditambahkan", ToastNotification.Type.SUCCESS);
                    refreshData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        top.add(searchField, BorderLayout.WEST);
        top.add(addBtn, BorderLayout.EAST);

        String[] cols = {"Kode", "Nama Barang", "Kategori", "Stok", "Status", "Aksi"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return col == 5; }
        };
        table = new JTable(model);
        table.setOpaque(false);
        table.setRowHeight(56);
        table.setFont(new Font("Inter", Font.PLAIN, 13));
        table.setForeground(new Color(51, 65, 85));
        table.setSelectionBackground(new Color(241, 245, 249));
        table.setSelectionForeground(new Color(51, 65, 85));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        table.getTableHeader().setForeground(new Color(100, 116, 139));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        table.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ActionEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        GlassPanel tablePanel = new GlassPanel(new BorderLayout());
        tablePanel.setRadius(16);
        tablePanel.add(top, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(tablePanel, BorderLayout.CENTER);
    }

    private void doSearch() {
        try {
            String q = searchField.getText();
            currentData = q.isEmpty() ? service.getAllBarang() : service.searchBarang(q);
            updateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        doSearch();
    }

    private void updateTable() {
        model.setRowCount(0);
        for (Barang b : currentData) {
            String status = b.isStokRendah() ? "LOW" : (b.isStokTinggi() ? "HIGH" : "NORMAL");
            model.addRow(new Object[]{b.getKode(), b.getNama(), b.getKategori(), 
                b.getStokSaatIni() + " " + b.getSatuan(), status, b.getId()});
        }
    }

    class ActionRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            p.setOpaque(false);
            JLabel edit = new JLabel(LucideIcon.createIcon(LucideIcon.IconName.EDIT2, 16, new Color(100, 116, 139)));
            JLabel del = new JLabel(LucideIcon.createIcon(LucideIcon.IconName.TRASH2, 16, new Color(220, 38, 38)));
            p.add(edit);
            p.add(del);
            return p;
        }
    }

    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int currentId;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            panel.setOpaque(false);
            JLabel edit = new JLabel(LucideIcon.createIcon(LucideIcon.IconName.EDIT2, 16, new Color(100, 116, 139)));
            JLabel del = new JLabel(LucideIcon.createIcon(LucideIcon.IconName.TRASH2, 16, new Color(220, 38, 38)));
            edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            del.setCursor(new Cursor(Cursor.HAND_CURSOR));

            edit.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    doEdit(currentId);
                }
            });
            del.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    doDelete(currentId);
                }
            });
            panel.add(edit);
            panel.add(del);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentId = (Integer) value;
            return panel;
        }

        public Object getCellEditorValue() { return currentId; }

        private void doEdit(int id) {
            try {
                Barang b = service.getBarangById(id);
                EditBarangDialog dialog = new EditBarangDialog(SwingUtilities.getWindowAncestor(BarangPanel.this), b);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    service.updateBarang(dialog.getBarang());
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(BarangPanel.this), "Barang berhasil diperbarui", ToastNotification.Type.SUCCESS);
                    refreshData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(BarangPanel.this, "Error: " + ex.getMessage());
            }
        }

        private void doDelete(int id) {
            int confirm = JOptionPane.showConfirmDialog(BarangPanel.this, "Yakin ingin menghapus barang ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.deleteBarang(id);
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(BarangPanel.this), "Barang dihapus", ToastNotification.Type.INFO);
                    refreshData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(BarangPanel.this, "Error: " + ex.getMessage());
                }
            }
        }
    }
}
