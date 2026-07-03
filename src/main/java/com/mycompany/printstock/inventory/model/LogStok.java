package com.mycompany.printstock.inventory.model;

import java.sql.Timestamp;

public class LogStok {
    private int id;
    private int pengajuanId;
    private int adminId;
    private Timestamp waktuDisetujui;

    private String namaAdmin;
    private String namaBarang;
    private String jenisMutasi;
    private int jumlah;

    public LogStok() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPengajuanId() { return pengajuanId; }
    public void setPengajuanId(int pengajuanId) { this.pengajuanId = pengajuanId; }
    
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    
    public Timestamp getWaktuDisetujui() { return waktuDisetujui; }
    public void setWaktuDisetujui(Timestamp waktuDisetujui) { this.waktuDisetujui = waktuDisetujui; }

    public String getNamaAdmin() { return namaAdmin; }
    public void setNamaAdmin(String namaAdmin) { this.namaAdmin = namaAdmin; }
    
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    
    public String getJenisMutasi() { return jenisMutasi; }
    public void setJenisMutasi(String jenisMutasi) { this.jenisMutasi = jenisMutasi; }
    
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
}