package com.mycompany.printstock.inventory.model;

public class Barang {
    private int id;
    private String kode;
    private String nama;
    private String kategori;
    private String satuan;
    private int stokMinimal;
    private int stokMaksimal;
    private int stokSaatIni;
    private int hargaBeli;
    private int hargaJual;

    public Barang() {}

    public Barang(int id, String kode, String nama, String kategori, String satuan,
                  int stokMinimal, int stokMaksimal, int stokSaatIni, int hargaBeli, int hargaJual) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.satuan = satuan;
        this.stokMinimal = stokMinimal;
        this.stokMaksimal = stokMaksimal;
        this.stokSaatIni = stokSaatIni;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public int getStokMinimal() { return stokMinimal; }
    public void setStokMinimal(int stokMinimal) { this.stokMinimal = stokMinimal; }
    public int getStokMaksimal() { return stokMaksimal; }
    public void setStokMaksimal(int stokMaksimal) { this.stokMaksimal = stokMaksimal; }
    public int getStokSaatIni() { return stokSaatIni; }
    public void setStokSaatIni(int stokSaatIni) { this.stokSaatIni = stokSaatIni; }
    public int getHargaBeli() { return hargaBeli; }
    public void setHargaBeli(int hargaBeli) { this.hargaBeli = hargaBeli; }
    public int getHargaJual() { return hargaJual; }
    public void setHargaJual(int hargaJual) { this.hargaJual = hargaJual; }

    public boolean isStokRendah() { return stokSaatIni <= stokMinimal; }
    public boolean isStokTinggi() { return stokSaatIni >= stokMaksimal * 0.8; }
}
