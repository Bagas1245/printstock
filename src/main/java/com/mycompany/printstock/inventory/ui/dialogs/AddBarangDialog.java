package com.mycompany.printstock.inventory.ui.dialogs;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.ui.components.*;

import javax.swing.*;
import java.awt.*;

public class AddBarangDialog extends JDialog {
    private boolean saved = false;
    private Barang barang = new Barang();

    private JTextField kodeField, namaField, satuanField, hargaBeliField, hargaJualField;
    private JComboBox<String> kategoriBox;
    private JSpinner stokMinSpinner, stokMaxSpinner;

    public AddBarangDialog(Window owner) {
        super(owner, "Tambah Barang Baru", ModalityType.APPLICATION_MODAL);
        setSize(480, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 16));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

        kodeField = addField(form, "Kode Barang", new JTextField("BRG-00X"));
        kategoriBox = addCombo(form, "Kategori", new String[]{"Kertas", "Tinta", "Alat Tulis", "Pelengkap", "Cetakan"});
        namaField = addField(form, "Nama Barang", new JTextField());
        satuanField = addField(form, "Satuan", new JTextField("Pcs"));
        stokMinSpinner = addSpinner(form, "Stok Minimal", 10);
        stokMaxSpinner = addSpinner(form, "Stok Maksimal", 100);
        hargaBeliField = addField(form, "Harga Beli (Rp)", new JTextField("0"));
        hargaJualField = addField(form, "Harga Jual (Rp)", new JTextField("0"));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        ModernButton cancel = new ModernButton("Batal");
        cancel.setGhost(true);
        cancel.addActionListener(e -> dispose());

        ModernButton save = new ModernButton("Simpan");
        save.addActionListener(e -> {
            barang.setKode(kodeField.getText());
            barang.setNama(namaField.getText());
            barang.setKategori((String) kategoriBox.getSelectedItem());
            barang.setSatuan(satuanField.getText());
            barang.setStokMinimal((Integer) stokMinSpinner.getValue());
            barang.setStokMaksimal((Integer) stokMaxSpinner.getValue());
            barang.setHargaBeli(parseInt(hargaBeliField.getText()));
            barang.setHargaJual(parseInt(hargaJualField.getText()));
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

    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Inter", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        p.add(lbl);
        JComboBox<String> box = new JComboBox<>(items);
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

    private int parseInt(String s) {
        try { return Integer.parseInt(s.replaceAll("[^0-9]", "")); } catch (Exception e) { return 0; }
    }

    public boolean isSaved() { return saved; }
    public Barang getBarang() { return barang; }
}
