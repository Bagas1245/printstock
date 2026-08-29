package com.mycompany.printstock.inventory.ui.dialogs;

import com.mycompany.printstock.inventory.model.User;
import com.mycompany.printstock.inventory.ui.components.ModernButton;
import javax.swing.*;
import java.awt.*;

public class AddUserDialog extends JDialog {
    private boolean saved = false;
    private User user = new User();
    
    private JTextField namaField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public AddUserDialog(Window owner) {
        super(owner, "Tambah Pengguna Baru", ModalityType.APPLICATION_MODAL);
        setSize(400, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

        form.add(createLabel("Nama Lengkap"));
        namaField = createTextField();
        form.add(namaField);

        form.add(createLabel("Username"));
        usernameField = createTextField();
        form.add(usernameField);

        form.add(createLabel("Password"));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Inter", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        form.add(passwordField);

        form.add(createLabel("Peran / Role"));
        roleBox = new JComboBox<>(new String[]{"Admin", "Staff Gudang", "Atasan"});
        roleBox.setFont(new Font("Inter", Font.PLAIN, 13));
        roleBox.setBackground(Color.WHITE);
        form.add(roleBox);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        ModernButton cancel = new ModernButton("Batal");
        cancel.setGhost(true);
        cancel.addActionListener(e -> dispose());

        ModernButton save = new ModernButton("Simpan");
        save.addActionListener(e -> {
            if (namaField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan Username tidak boleh kosong!");
                return;
            }
            user.setNama(namaField.getText());
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.setRole((String) roleBox.getSelectedItem());
            saved = true;
            dispose();
        });

        btnPanel.add(cancel);
        btnPanel.add(save);

        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Inter", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Inter", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    public boolean isSaved() { return saved; }
    public User getUser() { return user; }
}