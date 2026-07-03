package com.mycompany.printstock.inventory.dao;

import com.mycompany.printstock.inventory.model.PengajuanStok;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PengajuanStokDAO {
    private final Connection conn;

    public PengajuanStokDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public void insert(PengajuanStok p) throws SQLException {
        String sql = "INSERT INTO pengajuan_stok (barang_id, petugas_id, jenis_mutasi, jumlah, tanggal, keterangan, status) VALUES (?, ?, ?, ?, ?, ?, 'PENDING')";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
        ps.setInt(1, p.getBarangId());
        ps.setInt(2, p.getPetugasId());
        ps.setString(3, p.getJenisMutasi());
        ps.setInt(4, p.getJumlah());
        ps.setDate(5, p.getTanggal());
        ps.setString(6, p.getKeterangan());
        ps.executeUpdate();
        ps.close();
    }

    public List<PengajuanStok> findAllPending() throws SQLException {
        List<PengajuanStok> list = new ArrayList<>();
        String sql = "SELECT p.*, b.nama AS nama_barang, u.nama AS nama_petugas " +
                     "FROM pengajuan_stok p " +
                     "JOIN barang b ON p.barang_id = b.id " +
                     "JOIN users u ON p.petugas_id = u.id " +
                     "WHERE p.status = 'PENDING' ORDER BY p.waktu_pengajuan ASC";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            PengajuanStok p = new PengajuanStok();
            p.setId(rs.getInt("id"));
            p.setBarangId(rs.getInt("barang_id"));
            p.setPetugasId(rs.getInt("petugas_id"));
            p.setJenisMutasi(rs.getString("jenis_mutasi"));
            p.setJumlah(rs.getInt("jumlah"));
            p.setTanggal(rs.getDate("tanggal"));
            p.setKeterangan(rs.getString("keterangan"));
            p.setStatus(rs.getString("status"));
            p.setWaktuPengajuan(rs.getTimestamp("waktu_pengajuan"));
            
            p.setNamaBarang(rs.getString("nama_barang"));
            p.setNamaPetugas(rs.getString("nama_petugas"));
            
            list.add(p);
        }
        rs.close();
        stmt.close();
        return list;
    }

    public void updateStatus(int idPengajuan, String newStatus) throws SQLException {
        String sql = "UPDATE pengajuan_stok SET status = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newStatus);
        ps.setInt(2, idPengajuan);
        ps.executeUpdate();
        ps.close();
    }
}