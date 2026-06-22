package com.mycompany.printstock.inventory.ui.dialogs;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.service.BarangService;
import com.mycompany.printstock.inventory.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StokKeluarDialog extends JDialog {
    private boolean saved = false;
    private int barangId;
    private int jumlah;
    private String keterangan;

    private JComboBox<BarangItem> barangBox;
    private JSpinner jumlahSpinner;
    private JTextArea keteranganArea;
    private JLabel stokLabel;
    
    private JLabel warningLabel; 
    private ModernButton saveBtn; 
    
    private List<Barang> barangList;

    public StokKeluarDialog(Window owner) {
        super(owner, "Input Stok Keluar", ModalityType.APPLICATION_MODAL);
        setSize(440, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        try {
            barangList = new BarangService().getAllBarang();
        } catch (SQLException e) {
            barangList = new java.util.ArrayList<>();
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 12, 12));
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
        
        barangBox.addActionListener(e -> {
            updateStokLabel();
            checkStokLimit(); 
        });
        form.add(barangBox);

        JLabel lbl2 = new JLabel("Jumlah Keluar");
        lbl2.setFont(new Font("Inter", Font.BOLD, 12));
        lbl2.setForeground(new Color(51, 65, 85));
        form.add(lbl2);
        
        jumlahSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
        jumlahSpinner.setFont(new Font("Inter", Font.PLAIN, 13));
        
        jumlahSpinner.addChangeListener(e -> checkStokLimit());
        form.add(jumlahSpinner);

        JPanel stokInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        stokInfoPanel.setOpaque(false);

        stokLabel = new JLabel("Stok tersedia: 0");
        stokLabel.setFont(new Font("Inter", Font.PLAIN, 11));
        stokLabel.setForeground(new Color(100, 116, 139));
        stokInfoPanel.add(stokLabel);

        warningLabel = new JLabel("  - Melebihi stok yang ada!");
        warningLabel.setFont(new Font("Inter", Font.BOLD, 11));
        warningLabel.setForeground(new Color(220, 38, 38));
        warningLabel.setVisible(false);
        stokInfoPanel.add(warningLabel);

        form.add(stokInfoPanel);

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

        saveBtn = new ModernButton("Simpan Stok Keluar");
        saveBtn.setBgColor(new Color(220, 38, 38));
        saveBtn.addActionListener(e -> {
            BarangItem item = (BarangItem) barangBox.getSelectedItem();
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu");
                return;
            }
            int val = (Integer) jumlahSpinner.getValue();
            if (val > item.barang.getStokSaatIni()) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!");
                return;
            }
            barangId = item.barang.getId();
            jumlah = val;
            keterangan = keteranganArea.getText();
            saved = true;
            dispose();
        });

        btnPanel.add(cancel);
        btnPanel.add(saveBtn);

        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        updateStokLabel();
        checkStokLimit();
    }

    private void updateStokLabel() {
        BarangItem item = (BarangItem) barangBox.getSelectedItem();
        if (item != null) {
            stokLabel.setText("Stok tersedia: " + item.barang.getStokSaatIni() + " " + item.barang.getSatuan());
        }
    }

    private void checkStokLimit() {
        BarangItem item = (BarangItem) barangBox.getSelectedItem();
        if (item != null && saveBtn != null) {
            int val = (Integer) jumlahSpinner.getValue();
            int currentStok = item.barang.getStokSaatIni();

            if (val > currentStok) {
                warningLabel.setVisible(true);
                saveBtn.setEnabled(false);
            } else {
                warningLabel.setVisible(false);
                saveBtn.setEnabled(true);
            }
        }
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