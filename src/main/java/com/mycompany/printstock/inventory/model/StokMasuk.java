package com.mycompany.printstock.inventory.model;

public class StokMasuk {
    private int id;
    private int barangId;
    private int jumlah;
    private String tanggal;
    private String keterangan;
    private Barang barang;

    public StokMasuk() {}

    public StokMasuk(int id, int barangId, int jumlah, String tanggal, String keterangan) {
        this.id = id;
        this.barangId = barangId;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBarangId() { return barangId; }
    public void setBarangId(int barangId) { this.barangId = barangId; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public Barang getBarang() { return barang; }
    public void setBarang(Barang barang) { this.barang = barang; }
}
