package com.mycompany.printstock.inventory.ui.dialogs;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.ui.components.*;

import javax.swing.*;
import java.awt.*;

public class EditBarangDialog extends JDialog {
    private boolean saved = false;
    private Barang barang;

    private JTextField kodeField, namaField, satuanField;
    private JComboBox<String> kategoriBox;
    private JSpinner stokMinSpinner, stokMaxSpinner;

    public EditBarangDialog(Window owner, Barang barang) {
        super(owner, "Edit Barang", ModalityType.APPLICATION_MODAL);
        this.barang = barang;
        setSize(480, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 16));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

        kodeField = addField(form, "Kode Barang", new JTextField(barang.getKode()));
        kategoriBox = addCombo(form, "Kategori", new String[]{"Kertas", "Tinta", "Alat Tulis", "Pelengkap", "Cetakan"}, barang.getKategori());
        namaField = addField(form, "Nama Barang", new JTextField(barang.getNama()));
        satuanField = addField(form, "Satuan", new JTextField(barang.getSatuan()));
        stokMinSpinner = addSpinner(form, "Stok Minimal", barang.getStokMinimal());
        stokMaxSpinner = addSpinner(form, "Stok Maksimal", barang.getStokMaksimal());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        ModernButton cancel = new ModernButton("Batal");
        cancel.setGhost(true);
        cancel.addActionListener(e -> dispose());

        ModernButton save = new ModernButton("Update");
        save.addActionListener(e -> {
            barang.setKode(kodeField.getText());
            barang.setNama(namaField.getText());
            barang.setKategori((String) kategoriBox.getSelectedItem());
            barang.setSatuan(satuanField.getText());
            barang.setStokMinimal((Integer) stokMinSpinner.getValue());
            barang.setStokMaksimal((Integer) stokMaxSpinner.getValue());
            saved = true;
            dispose();
        });

        btnPanel.add(cancel);
        btnPanel.add(save);

        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField addField(JPanel p, String label, JTextField field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Inter", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        p.add(lbl);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Inter", Font.PLAIN, 13));
        p.add(field);
        return field;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items, String selected) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Inter", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        p.add(lbl);
        JComboBox<String> box = new JComboBox<>(items);
        box.setSelectedItem(selected);
        box.setFont(new Font("Inter", Font.PLAIN, 13));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true));
        p.add(box);
        return box;
    }

    private JSpinner addSpinner(JPanel p, String label, int val) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Inter", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        p.add(lbl);
        JSpinner spin = new JSpinner(new SpinnerNumberModel(val, 0, 99999, 1));
        spin.setFont(new Font("Inter", Font.PLAIN, 13));
        p.add(spin);
        return spin;
    }

    public boolean isSaved() { return saved; }
    public Barang getBarang() { return barang; }
}
