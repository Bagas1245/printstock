package com.mycompany.printstock.inventory.model;

import java.sql.Date;
import java.sql.Timestamp;

public class PengajuanStok {
    private int id;
    private int barangId;
    private int petugasId;
    private String jenisMutasi;
    private int jumlah;
    private Date tanggal;
    private String keterangan;
    private String status;
    private Timestamp waktuPengajuan;

    private String namaBarang;
    private String namaPetugas;

    public PengajuanStok() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getBarangId() { return barangId; }
    public void setBarangId(int barangId) { this.barangId = barangId; }
    
    public int getPetugasId() { return petugasId; }
    public void setPetugasId(int petugasId) { this.petugasId = petugasId; }
    
    public String getJenisMutasi() { return jenisMutasi; }
    public void setJenisMutasi(String jenisMutasi) { this.jenisMutasi = jenisMutasi; }
    
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    
    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getWaktuPengajuan() { return waktuPengajuan; }
    public void setWaktuPengajuan(Timestamp waktuPengajuan) { this.waktuPengajuan = waktuPengajuan; }

    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    
    public String getNamaPetugas() { return namaPetugas; }
    public void setNamaPetugas(String namaPetugas) { this.namaPetugas = namaPetugas; }
}