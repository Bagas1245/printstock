package com.mycompany.printstock.inventory.service;

import com.mycompany.printstock.inventory.dao.BarangDAO;
import com.mycompany.printstock.inventory.dao.StokMasukDAO;
import com.mycompany.printstock.inventory.dao.StokKeluarDAO;
import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.StokMasuk;
import com.mycompany.printstock.inventory.model.StokKeluar;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StokService {
    private final StokMasukDAO stokMasukDAO;
    private final StokKeluarDAO stokKeluarDAO;
    private final BarangDAO barangDAO;

    public StokService() {
        this.stokMasukDAO = new StokMasukDAO();
        this.stokKeluarDAO = new StokKeluarDAO();
        this.barangDAO = new BarangDAO();
    }

    public List<StokMasuk> getAllStokMasuk() throws SQLException {
        return stokMasukDAO.findAll();
    }

    public List<StokKeluar> getAllStokKeluar() throws SQLException {
        return stokKeluarDAO.findAll();
    }

    public void addStokMasuk(int barangId, int jumlah, String keterangan) throws SQLException {
        Barang barang = barangDAO.findById(barangId);
        if (barang == null) throw new IllegalArgumentException("Barang tidak ditemukan");

        StokMasuk masuk = new StokMasuk();
        masuk.setBarangId(barangId);
        masuk.setJumlah(jumlah);
        masuk.setTanggal(LocalDate.now().toString());
        masuk.setKeterangan(keterangan);
        stokMasukDAO.insert(masuk);

        barangDAO.updateStok(barangId, barang.getStokSaatIni() + jumlah);
    }

    public void addStokKeluar(int barangId, int jumlah, String keterangan) throws SQLException {
        Barang barang = barangDAO.findById(barangId);
        if (barang == null) throw new IllegalArgumentException("Barang tidak ditemukan");
        if (barang.getStokSaatIni() < jumlah) throw new IllegalArgumentException("Stok tidak mencukupi!");

        StokKeluar keluar = new StokKeluar();
        keluar.setBarangId(barangId);
        keluar.setJumlah(jumlah);
        keluar.setTanggal(LocalDate.now().toString());
        keluar.setKeterangan(keterangan);
        stokKeluarDAO.insert(keluar);

        barangDAO.updateStok(barangId, barang.getStokSaatIni() - jumlah);
    }
}
