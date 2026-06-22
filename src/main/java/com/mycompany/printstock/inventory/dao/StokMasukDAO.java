package com.mycompany.printstock.inventory.dao;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.StokMasuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StokMasukDAO {
    private final Connection conn;

    public StokMasukDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public List<StokMasuk> findAll() throws SQLException {
        List<StokMasuk> list = new ArrayList<>();
        String sql = "SELECT sm.*, b.nama as barang_nama, b.kategori, b.satuan FROM stok_masuk sm JOIN barang b ON sm.barang_id = b.id ORDER BY sm.tanggal DESC, sm.id DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        rs.close();
        stmt.close();
        return list;
    }

    public void insert(StokMasuk s) throws SQLException {
        String sql = "INSERT INTO stok_masuk (barang_id, jumlah, tanggal, keterangan) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, s.getBarangId());
        ps.setInt(2, s.getJumlah());
        ps.setString(3, s.getTanggal());
        ps.setString(4, s.getKeterangan());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            s.setId(rs.getInt(1));
        }
        rs.close();
        ps.close();
    }

    private StokMasuk mapResultSet(ResultSet rs) throws SQLException {
        StokMasuk s = new StokMasuk();
        s.setId(rs.getInt("id"));
        s.setBarangId(rs.getInt("barang_id"));
        s.setJumlah(rs.getInt("jumlah"));
        s.setTanggal(rs.getString("tanggal"));
        s.setKeterangan(rs.getString("keterangan"));

        Barang b = new Barang();
        b.setId(rs.getInt("barang_id"));
        b.setNama(rs.getString("barang_nama"));
        b.setKategori(rs.getString("kategori"));
        b.setSatuan(rs.getString("satuan"));
        s.setBarang(b);
        return s;
    }
}
