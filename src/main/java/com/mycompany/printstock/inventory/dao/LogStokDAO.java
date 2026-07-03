package com.mycompany.printstock.inventory.dao;

import com.mycompany.printstock.inventory.model.LogStok;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogStokDAO {
    private final Connection conn;

    public LogStokDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public void insert(int pengajuanId, int adminId) throws SQLException {
        String sql = "INSERT INTO log_stok (pengajuan_id, admin_id) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pengajuanId);
        ps.setInt(2, adminId);
        ps.executeUpdate();
        ps.close();
    }

    public List<LogStok> findAllLogs() throws SQLException {
        List<LogStok> list = new ArrayList<>();
        String sql = "SELECT l.*, a.nama AS nama_admin, b.nama AS nama_barang, p.jenis_mutasi, p.jumlah " +
                     "FROM log_stok l " +
                     "JOIN users a ON l.admin_id = a.id " +
                     "JOIN pengajuan_stok p ON l.pengajuan_id = p.id " +
                     "JOIN barang b ON p.barang_id = b.id " +
                     "ORDER BY l.waktu_disetujui DESC";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            LogStok l = new LogStok();
            l.setId(rs.getInt("id"));
            l.setPengajuanId(rs.getInt("pengajuan_id"));
            l.setAdminId(rs.getInt("admin_id"));
            l.setWaktuDisetujui(rs.getTimestamp("waktu_disetujui"));
            
            l.setNamaAdmin(rs.getString("nama_admin"));
            l.setNamaBarang(rs.getString("nama_barang"));
            l.setJenisMutasi(rs.getString("jenis_mutasi"));
            l.setJumlah(rs.getInt("jumlah"));
            
            list.add(l);
        }
        rs.close();
        stmt.close();
        return list;
    }
}