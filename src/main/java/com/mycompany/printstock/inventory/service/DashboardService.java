package com.mycompany.printstock.inventory.service;

import com.mycompany.printstock.inventory.dao.BarangDAO;
import com.mycompany.printstock.inventory.dao.StokMasukDAO;
import com.mycompany.printstock.inventory.dao.StokKeluarDAO;
import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.StokMasuk;
import com.mycompany.printstock.inventory.model.StokKeluar;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DashboardService {
    private final BarangDAO barangDAO;
    private final StokMasukDAO stokMasukDAO;
    private final StokKeluarDAO stokKeluarDAO;

    public DashboardService() {
        this.barangDAO = new BarangDAO();
        this.stokMasukDAO = new StokMasukDAO();
        this.stokKeluarDAO = new StokKeluarDAO();
    }

    public int getTotalBarang() throws SQLException {
        return barangDAO.findAll().size();
    }

    public int getTotalStok() throws SQLException {
        return barangDAO.findAll().stream().mapToInt(Barang::getStokSaatIni).sum();
    }

    public List<Barang> getStokRendah() throws SQLException {
        List<Barang> result = new ArrayList<>();
        for (Barang b : barangDAO.findAll()) {
            if (b.isStokRendah()) result.add(b);
        }
        return result;
    }

    public List<Barang> getStokTinggi() throws SQLException {
        List<Barang> result = new ArrayList<>();
        for (Barang b : barangDAO.findAll()) {
            if (b.isStokTinggi()) result.add(b);
        }
        return result;
    }

    public List<StokMasuk> getRecentStokMasuk(int days) throws SQLException {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        List<StokMasuk> result = new ArrayList<>();
        for (StokMasuk s : stokMasukDAO.findAll()) {
            if (LocalDate.parse(s.getTanggal()).isAfter(cutoff) || LocalDate.parse(s.getTanggal()).isEqual(cutoff)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<StokKeluar> getRecentStokKeluar(int days) throws SQLException {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        List<StokKeluar> result = new ArrayList<>();
        for (StokKeluar s : stokKeluarDAO.findAll()) {
            if (LocalDate.parse(s.getTanggal()).isAfter(cutoff) || LocalDate.parse(s.getTanggal()).isEqual(cutoff)) {
                result.add(s);
            }
        }
        return result;
    }

    public Map<String, int[]> getWeeklyActivity() throws SQLException {
        Map<String, int[]> data = new LinkedHashMap<>();
        String[] dayNames = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};

        for (int i = 6; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusDays(i);
            String label = dayNames[d.getDayOfWeek().getValue() % 7];
            data.put(label, new int[]{0, 0});
        }

        for (StokMasuk s : stokMasukDAO.findAll()) {
            LocalDate d = LocalDate.parse(s.getTanggal());
            long diff = java.time.temporal.ChronoUnit.DAYS.between(d, LocalDate.now());
            if (diff >= 0 && diff <= 6) {
                String label = dayNames[d.getDayOfWeek().getValue() % 7];
                data.get(label)[0] += s.getJumlah();
            }
        }

        for (StokKeluar s : stokKeluarDAO.findAll()) {
            LocalDate d = LocalDate.parse(s.getTanggal());
            long diff = java.time.temporal.ChronoUnit.DAYS.between(d, LocalDate.now());
            if (diff >= 0 && diff <= 6) {
                String label = dayNames[d.getDayOfWeek().getValue() % 7];
                data.get(label)[1] += s.getJumlah();
            }
        }

        return data;
    }

    public List<Map.Entry<Barang, Integer>> getTopUsage(int days) throws SQLException {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        Map<Integer, Integer> usageMap = new HashMap<>();

        for (StokKeluar s : stokKeluarDAO.findAll()) {
            LocalDate d = LocalDate.parse(s.getTanggal());
            if (!d.isBefore(cutoff)) {
                usageMap.merge(s.getBarangId(), s.getJumlah(), Integer::sum);
            }
        }

        List<Map.Entry<Barang, Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : usageMap.entrySet()) {
            Barang b = barangDAO.findById(entry.getKey());
            if (b != null) {
                result.add(new AbstractMap.SimpleEntry<>(b, entry.getValue()));
            }
        }
        result.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return result;
    }
    
    public List<StokMasuk> getStokMasukByDateRange(LocalDate start, LocalDate end) throws SQLException {
        List<StokMasuk> result = new ArrayList<>();
        for (StokMasuk s : stokMasukDAO.findAll()) {
            LocalDate date = LocalDate.parse(s.getTanggal());
            if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) {
                result.add(s);
            }
        }
        return result;
    }

    public List<StokKeluar> getStokKeluarByDateRange(LocalDate start, LocalDate end) throws SQLException {
        List<StokKeluar> result = new ArrayList<>();
        for (StokKeluar s : stokKeluarDAO.findAll()) {
            LocalDate date = LocalDate.parse(s.getTanggal());
            if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) {
                result.add(s);
            }
        }
        return result;
    }
    
}
