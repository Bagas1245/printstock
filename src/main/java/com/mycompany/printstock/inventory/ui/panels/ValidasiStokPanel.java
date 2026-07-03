package com.mycompany.printstock.inventory.ui.panels;

import com.mycompany.printstock.inventory.dao.BarangDAO;
import com.mycompany.printstock.inventory.dao.LogStokDAO;
import com.mycompany.printstock.inventory.dao.PengajuanStokDAO;
import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.PengajuanStok;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ValidasiStokPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSetujui;
    private JButton btnTolak;
    private JButton btnRefresh;

    private PengajuanStokDAO pengajuanDAO;
    private BarangDAO barangDAO;
    private LogStokDAO logDAO;

    private List<PengajuanStok> pendingList;

    public ValidasiStokPanel() {
        pengajuanDAO = new PengajuanStokDAO();
        barangDAO = new BarangDAO();
        logDAO = new LogStokDAO();

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Validasi Pengajuan Stok dari Gudang");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Tanggal", "Nama Barang", "Petugas", "Mutasi", "Jumlah", "Keterangan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        btnRefresh = new JButton("Refresh Data");
        btnTolak = new JButton("Tolak Pengajuan");
        btnSetujui = new JButton("Setujui Pengajuan");

        btnSetujui.setBackground(new Color(40, 167, 69));
        btnSetujui.setForeground(Color.WHITE);
        btnTolak.setBackground(new Color(220, 53, 69));
        btnTolak.setForeground(Color.WHITE);

        actionPanel.add(btnRefresh);
        actionPanel.add(btnTolak);
        actionPanel.add(btnSetujui);

        add(actionPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        btnSetujui.addActionListener(e -> approvePengajuan());
        btnTolak.addActionListener(e -> rejectPengajuan());
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            pendingList = pengajuanDAO.findAllPending();

            for (PengajuanStok p : pendingList) {
                Object[] row = {
                    p.getId(),
                    p.getTanggal(),
                    p.getNamaBarang(),
                    p.getNamaPetugas(),
                    p.getJenisMutasi(),
                    p.getJumlah(),
                    p.getKeterangan()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void approvePengajuan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pengajuan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menyetujui pengajuan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            PengajuanStok pengajuan = pendingList.get(selectedRow);

            pengajuanDAO.updateStatus(pengajuan.getId(), "APPROVED");

            Barang barang = barangDAO.findById(pengajuan.getBarangId());
            int stokBaru = barang.getStokSaatIni();
            
            if (pengajuan.getJenisMutasi().equals("MASUK")) {
                stokBaru += pengajuan.getJumlah();
            } else {
                stokBaru -= pengajuan.getJumlah();
            }
            barangDAO.updateStok(barang.getId(), stokBaru);

            int idAdminLogin = 1; 
            logDAO.insert(pengajuan.getId(), idAdminLogin);

            JOptionPane.showMessageDialog(this, "Pengajuan berhasil disetujui! Stok telah diperbarui.");
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyetujui pengajuan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void rejectPengajuan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pengajuan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin MENOLAK pengajuan ini?", "Konfirmasi Tolak", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            PengajuanStok pengajuan = pendingList.get(selectedRow);
            
            pengajuanDAO.updateStatus(pengajuan.getId(), "REJECTED");
            
            JOptionPane.showMessageDialog(this, "Pengajuan berhasil ditolak.");
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menolak pengajuan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public void refreshData() {
        loadData();
    }
}