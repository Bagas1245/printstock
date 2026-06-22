package com.mycompany.printstock.inventory.dao;

import com.mycompany.printstock.inventory.model.Barang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO {
    private final Connection conn;

    public BarangDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public List<Barang> findAll() throws SQLException {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang ORDER BY id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        rs.close();
        stmt.close();
        return list;
    }

    public List<Barang> search(String query) throws SQLException {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE LOWER(nama) LIKE ? OR LOWER(kode) LIKE ? ORDER BY id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + query.toLowerCase() + "%");
        ps.setString(2, "%" + query.toLowerCase() + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        rs.close();
        ps.close();
        return list;
    }

    public Barang findById(int id) throws SQLException {
        String sql = "SELECT * FROM barang WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Barang b = null;
        if (rs.next()) {
            b = mapResultSet(rs);
        }
        rs.close();
        ps.close();
        return b;
    }

    public void insert(Barang b) throws SQLException {
        String sql = "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, b.getKode());
        ps.setString(2, b.getNama());
        ps.setString(3, b.getKategori());
        ps.setString(4, b.getSatuan());
        ps.setInt(5, b.getStokMinimal());
        ps.setInt(6, b.getStokMaksimal());
        ps.setInt(7, b.getStokSaatIni());
        ps.setInt(8, b.getHargaBeli());
        ps.setInt(9, b.getHargaJual());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            b.setId(rs.getInt(1));
        }
        rs.close();
        ps.close();
    }

    public void update(Barang b) throws SQLException {
        String sql = "UPDATE barang SET kode=?, nama=?, kategori=?, satuan=?, stok_minimal=?, stok_maksimal=?, stok_saat_ini=?, harga_beli=?, harga_jual=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, b.getKode());
        ps.setString(2, b.getNama());
        ps.setString(3, b.getKategori());
        ps.setString(4, b.getSatuan());
        ps.setInt(5, b.getStokMinimal());
        ps.setInt(6, b.getStokMaksimal());
        ps.setInt(7, b.getStokSaatIni());
        ps.setInt(8, b.getHargaBeli());
        ps.setInt(9, b.getHargaJual());
        ps.setInt(10, b.getId());
        ps.executeUpdate();
        ps.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM barang WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }

    public void updateStok(int id, int newStok) throws SQLException {
        String sql = "UPDATE barang SET stok_saat_ini = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newStok);
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close();
    }

    private Barang mapResultSet(ResultSet rs) throws SQLException {
        Barang b = new Barang();
        b.setId(rs.getInt("id"));
        b.setKode(rs.getString("kode"));
        b.setNama(rs.getString("nama"));
        b.setKategori(rs.getString("kategori"));
        b.setSatuan(rs.getString("satuan"));
        b.setStokMinimal(rs.getInt("stok_minimal"));
        b.setStokMaksimal(rs.getInt("stok_maksimal"));
        b.setStokSaatIni(rs.getInt("stok_saat_ini"));
        b.setHargaBeli(rs.getInt("harga_beli"));
        b.setHargaJual(rs.getInt("harga_jual"));
        return b;
    }
}
