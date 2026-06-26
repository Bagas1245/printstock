package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.ui.components.LucideIcon;
import com.mycompany.printstock.inventory.ui.components.ModernButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManajemenUserPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManajemenUserPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 24));
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Manajemen Pengguna");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));
        topPanel.add(titleLabel, BorderLayout.WEST);

        ModernButton btnAddUser = new ModernButton("Tambah Pengguna");
        btnAddUser.setIcon(LucideIcon.createIcon(LucideIcon.IconName.PLUS, 16, Color.WHITE));
        btnAddUser.addActionListener(e -> tambahPengguna());
        topPanel.add(btnAddUser, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Nama Lengkap", "Username", "Role / Peran"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setRowHeight(36);
        userTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        userTable.setFont(new Font("Inter", Font.PLAIN, 13));
        userTable.setShowVerticalLines(false);
        userTable.setSelectionBackground(new Color(241, 245, 249));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        add(scrollPane, BorderLayout.CENTER);
        
        refreshData();
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"1", "Bagas Mishbahuddin", "bagas", "Admin"});
        tableModel.addRow(new Object[]{"2", "Ahmad Pegawai", "ahmad", "Staff Gudang"});
        tableModel.addRow(new Object[]{"3", "Bapak Direktur", "direktur", "Atasan"});
    }

    private void tambahPengguna() {
        JOptionPane.showMessageDialog(this, "Fitur form tambah pengguna akan muncul di sini.");
    }
}
