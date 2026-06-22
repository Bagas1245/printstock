package com.mycompany.printstock.inventory.dao;

import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.StokKeluar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StokKeluarDAO {
    private final Connection conn;

    public StokKeluarDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public List<StokKeluar> findAll() throws SQLException {
        List<StokKeluar> list = new ArrayList<>();
        String sql = "SELECT sk.*, b.nama as barang_nama, b.kategori, b.satuan FROM stok_keluar sk JOIN barang b ON sk.barang_id = b.id ORDER BY sk.tanggal DESC, sk.id DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        rs.close();
        stmt.close();
        return list;
    }

    public void insert(StokKeluar s) throws SQLException {
        String sql = "INSERT INTO stok_keluar (barang_id, jumlah, tanggal, keterangan) VALUES (?, ?, ?, ?)";
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

    private StokKeluar mapResultSet(ResultSet rs) throws SQLException {
        StokKeluar s = new StokKeluar();
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
