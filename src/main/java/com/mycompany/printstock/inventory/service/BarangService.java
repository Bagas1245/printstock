package com.mycompany.printstock.inventory.service;

import com.mycompany.printstock.inventory.dao.BarangDAO;
import com.mycompany.printstock.inventory.model.Barang;
import java.sql.SQLException;
import java.util.List;

public class BarangService {
    private final BarangDAO barangDAO;

    public BarangService() {
        this.barangDAO = new BarangDAO();
    }

    public List<Barang> getAllBarang() throws SQLException {
        return barangDAO.findAll();
    }

    public List<Barang> searchBarang(String query) throws SQLException {
        return barangDAO.search(query);
    }

    public Barang getBarangById(int id) throws SQLException {
        return barangDAO.findById(id);
    }

    public void addBarang(Barang barang) throws SQLException {
        barang.setStokSaatIni(0);
        barangDAO.insert(barang);
    }

    public void updateBarang(Barang barang) throws SQLException {
        barangDAO.update(barang);
    }

    public void deleteBarang(int id) throws SQLException {
        barangDAO.delete(id);
    }
}
