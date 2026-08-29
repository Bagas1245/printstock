package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.dao.UserDAO;
import com.mycompany.printstock.inventory.model.User;
import com.mycompany.printstock.inventory.ui.components.LucideIcon;
import com.mycompany.printstock.inventory.ui.components.ModernButton;
import com.mycompany.printstock.inventory.ui.components.ToastNotification;
import com.mycompany.printstock.inventory.ui.dialogs.AddUserDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManajemenUserPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    private UserDAO userDAO = new UserDAO();

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
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
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
        List<User> users = userDAO.getAllUsers();
        
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getId(), 
                u.getNama(), 
                u.getUsername(), 
                u.getRole()
            });
        }
    }

    private void tambahPengguna() {
        AddUserDialog dialog = new AddUserDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            User newUser = dialog.getUser();
            boolean success = userDAO.insert(newUser);
            
            if (success) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Pengguna berhasil ditambahkan", ToastNotification.Type.SUCCESS);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan pengguna ke database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
