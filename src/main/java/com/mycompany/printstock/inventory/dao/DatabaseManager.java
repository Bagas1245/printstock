package com.mycompany.printstock.inventory.dao;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/printstock";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            connection.setAutoCommit(true);
            initializeSchema();
            seedData();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver tidak ditemukan. Pastikan mysql-connector-j sudah ada di classpath.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal koneksi ke MySQL. Pastikan server MySQL berjalan dan database 'printstock' sudah dibuat.", e);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeSchema() throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS barang (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "kode VARCHAR(20) NOT NULL UNIQUE, " +
            "nama VARCHAR(100) NOT NULL, " +
            "kategori VARCHAR(50), " +
            "satuan VARCHAR(20), " +
            "stok_minimal INT DEFAULT 0, " +
            "stok_maksimal INT DEFAULT 0, " +
            "stok_saat_ini INT DEFAULT 0, " +
            "harga_beli INT DEFAULT 0, " +
            "harga_jual INT DEFAULT 0)");

        stmt.execute("CREATE TABLE IF NOT EXISTS stok_masuk (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "barang_id INT NOT NULL, " +
            "jumlah INT NOT NULL, " +
            "tanggal DATE NOT NULL, " +
            "keterangan TEXT, " +
            "FOREIGN KEY (barang_id) REFERENCES barang(id) ON DELETE CASCADE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS stok_keluar (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "barang_id INT NOT NULL, " +
            "jumlah INT NOT NULL, " +
            "tanggal DATE NOT NULL, " +
            "keterangan TEXT, " +
            "FOREIGN KEY (barang_id) REFERENCES barang(id) ON DELETE CASCADE)");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "nama VARCHAR(100) NOT NULL, " +
            "username VARCHAR(50) NOT NULL UNIQUE, " +
            "password VARCHAR(255) NOT NULL, " +
            "role VARCHAR(20) NOT NULL)");

        stmt.close();
    }

    private void seedData() throws SQLException {
        Statement check = connection.createStatement();
        ResultSet rs = check.executeQuery("SELECT COUNT(*) as count FROM barang");
        if (rs.next() && rs.getInt("count") > 0) {
            check.close();
            return;
        }
        check.close();

        String[] barangSql = {
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-001', 'Kertas A4 80gsm', 'Kertas', 'Rim', 10, 200, 45, 45000, 55000)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-002', 'Kertas F4 80gsm', 'Kertas', 'Rim', 8, 150, 5, 52000, 65000)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-003', 'Tinta Printer Hitam', 'Tinta', 'Botol', 5, 50, 12, 85000, 100000)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-004', 'Tinta Printer Warna', 'Tinta', 'Botol', 5, 50, 48, 90000, 110000)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-005', 'Lakban Bening 2inch', 'Alat Tulis', 'Pcs', 20, 200, 150, 8000, 12000)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-006', 'Plastik Cover A4', 'Pelengkap', 'Pcs', 50, 500, 320, 1500, 2500)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-007', 'Stiker Vinyl A3', 'Kertas', 'Lembar', 15, 100, 8, 18000, 25000)",
            "INSERT INTO barang (kode, nama, kategori, satuan, stok_minimal, stok_maksimal, stok_saat_ini, harga_beli, harga_jual) VALUES ('BRG-008', 'Spanduk Flexy 1x1m', 'Cetakan', 'Meter', 5, 100, 60, 35000, 50000)"
        };

        String[] masukSql = {
            "INSERT INTO stok_masuk (barang_id, jumlah, tanggal, keterangan) VALUES (1, 50, '2026-04-15', 'Pembelian dari distributor')",
            "INSERT INTO stok_masuk (barang_id, jumlah, tanggal, keterangan) VALUES (3, 20, '2026-04-16', 'Restock bulanan')",
            "INSERT INTO stok_masuk (barang_id, jumlah, tanggal, keterangan) VALUES (2, 30, '2026-04-10', 'Pembelian awal')"
        };

        String[] keluarSql = {
            "INSERT INTO stok_keluar (barang_id, jumlah, tanggal, keterangan) VALUES (1, 20, '2026-04-17', 'Penjualan harian')",
            "INSERT INTO stok_keluar (barang_id, jumlah, tanggal, keterangan) VALUES (2, 15, '2026-04-18', 'Cetakan raport')",
            "INSERT INTO stok_keluar (barang_id, jumlah, tanggal, keterangan) VALUES (5, 30, '2026-04-18', 'Packing pesanan')",
            "INSERT INTO stok_keluar (barang_id, jumlah, tanggal, keterangan) VALUES (6, 100, '2026-04-17', 'Proposal tender')"
        };

        Statement stmt = connection.createStatement();
        for (String sql : barangSql) stmt.execute(sql);
        for (String sql : masukSql) stmt.execute(sql);
        for (String sql : keluarSql) stmt.execute(sql);
        stmt.close();
    }
}

