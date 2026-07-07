package com.mycompany.printstock.inventory.service;

import com.mycompany.printstock.inventory.dao.BarangDAO;
import com.mycompany.printstock.inventory.dao.DatabaseManager;
import com.mycompany.printstock.inventory.dao.LogStokDAO;
import com.mycompany.printstock.inventory.model.Barang;
import com.mycompany.printstock.inventory.model.LogStok;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DashboardService {
    private final BarangDAO barangDAO;
    private final LogStokDAO logStokDAO;

    public DashboardService() {
        this.barangDAO = new BarangDAO();
        this.logStokDAO = new LogStokDAO();
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

    public List<LogStok> getRecentLogs(int days) throws SQLException {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        List<LogStok> result = new ArrayList<>();
        for (LogStok log : logStokDAO.findAllLogs()) {
            LocalDate logDate = log.getWaktuDisetujui().toLocalDateTime().toLocalDate();
            if (!logDate.isBefore(cutoff)) {
                result.add(log);
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

        for (LogStok log : logStokDAO.findAllLogs()) {
            LocalDate d = log.getWaktuDisetujui().toLocalDateTime().toLocalDate();
            long diff = java.time.temporal.ChronoUnit.DAYS.between(d, LocalDate.now());
            if (diff >= 0 && diff <= 6) {
                String label = dayNames[d.getDayOfWeek().getValue() % 7];
                if ("MASUK".equals(log.getJenisMutasi())) {
                    data.get(label)[0] += log.getJumlah();
                } else {
                    data.get(label)[1] += log.getJumlah();
                }
            }
        }
        return data;
    }
    
    public List<LogStok> getLogsByDateRange(LocalDate start, LocalDate end) throws SQLException {
        List<LogStok> result = new ArrayList<>();
        for (LogStok log : logStokDAO.findAllLogs()) {
            LocalDate d = log.getWaktuDisetujui().toLocalDateTime().toLocalDate();
            if ((d.isEqual(start) || d.isAfter(start)) && (d.isEqual(end) || d.isBefore(end))) {
                result.add(log);
            }
        }
        return result;
    }

    public Map<String, Integer> getDashboardStats() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total_barang", getTotalBarang());
        
        int masuk = 0;
        int keluar = 0;
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        
        for (LogStok log : logStokDAO.findAllLogs()) {
            LocalDate logDate = log.getWaktuDisetujui().toLocalDateTime().toLocalDate();
            if (!logDate.isBefore(startOfMonth)) {
                if ("MASUK".equals(log.getJenisMutasi())) masuk += log.getJumlah();
                else keluar += log.getJumlah();
            }
        }
        stats.put("masuk_bulan_ini", masuk);
        stats.put("keluar_bulan_ini", keluar);
        return stats;
    }

    public List<Barang> getBarangStokMenipis() throws SQLException {
        List<Barang> list = new ArrayList<>();
        String query = "SELECT id, kode, nama, stok_saat_ini, satuan FROM barang WHERE stok_saat_ini <= 10 ORDER BY stok_saat_ini ASC LIMIT 5";
        
        java.sql.Connection conn = DatabaseManager.getInstance().getConnection();
        
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Barang b = new Barang();
                b.setId(rs.getInt("id"));
                b.setKode(rs.getString("kode"));
                b.setNama(rs.getString("nama"));
                b.setStokSaatIni(rs.getInt("stok_saat_ini")); 
                b.setSatuan(rs.getString("satuan"));
                list.add(b);
            }
        }
        return list;
    }
}