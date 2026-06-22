package com.mycompany.printstock.inventory.ui.dialogs;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.service.BarangService;
import com.mycompany.printstock.inventory.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StokMasukDialog extends JDialog {
    private boolean saved = false;
    private int barangId;
    private int jumlah;
    private String keterangan;

    private JComboBox<BarangItem> barangBox;
    private JSpinner jumlahSpinner;
    private JTextArea keteranganArea;
    private List<Barang> barangList;

    public StokMasukDialog(Window owner) {
        super(owner, "Input Stok Masuk", ModalityType.APPLICATION_MODAL);
        setSize(440, 380);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        try {
            barangList = new BarangService().getAllBarang();
        } catch (SQLException e) {
            barangList = new java.util.ArrayList<>();
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 12, 16));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

        JLabel lbl1 = new JLabel("Pilih Barang");
        lbl1.setFont(new Font("Inter", Font.BOLD, 12));
        lbl1.setForeground(new Color(51, 65, 85));
        form.add(lbl1);

        barangBox = new JComboBox<>();
        for (Barang b : barangList) {
            barangBox.addItem(new BarangItem(b));
        }
        barangBox.setFont(new Font("Inter", Font.PLAIN, 13));
        barangBox.setBackground(Color.WHITE);
        form.add(barangBox);

        JLabel lbl2 = new JLabel("Jumlah Masuk");
        lbl2.setFont(new Font("Inter", Font.BOLD, 12));
        lbl2.setForeground(new Color(51, 65, 85));
        form.add(lbl2);
        jumlahSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
        jumlahSpinner.setFont(new Font("Inter", Font.PLAIN, 13));
        form.add(jumlahSpinner);

        JLabel lbl3 = new JLabel("Keterangan");
        lbl3.setFont(new Font("Inter", Font.BOLD, 12));
        lbl3.setForeground(new Color(51, 65, 85));
        form.add(lbl3);
        keteranganArea = new JTextArea(3, 20);
        keteranganArea.setFont(new Font("Inter", Font.PLAIN, 13));
        keteranganArea.setLineWrap(true);
        keteranganArea.setWrapStyleWord(true);
        keteranganArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        form.add(new JScrollPane(keteranganArea));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        ModernButton cancel = new ModernButton("Batal");
        cancel.setGhost(true);
        cancel.addActionListener(e -> dispose());

        ModernButton save = new ModernButton("Simpan Stok Masuk");
        save.setBgColor(new Color(16, 185, 129));
        save.addActionListener(e -> {
            BarangItem item = (BarangItem) barangBox.getSelectedItem();
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu");
                return;
            }
            barangId = item.barang.getId();
            jumlah = (Integer) jumlahSpinner.getValue();
            keterangan = keteranganArea.getText();
            saved = true;
            dispose();
        });

        btnPanel.add(cancel);
        btnPanel.add(save);

        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public boolean isSaved() { return saved; }
    public int getBarangId() { return barangId; }
    public int getJumlah() { return jumlah; }
    public String getKeterangan() { return keterangan; }

    static class BarangItem {
        Barang barang;
        BarangItem(Barang b) { this.barang = b; }
        public String toString() {
            return barang.getKode() + " - " + barang.getNama() + " (Stok: " + barang.getStokSaatIni() + ")";
        }
    }
}
