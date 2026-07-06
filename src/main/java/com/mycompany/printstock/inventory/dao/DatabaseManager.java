package com.mycompany.printstock.inventory.dao;

import java.sql.*;

public class DatabaseManager {
    private static final String HOST = "aws-1-ap-northeast-2.pooler.supabase.com";
    private static final String PORT = "6543"; 
    private static final String DB_USER = "postgres.wdnifyhdkswtysfovxhn"; 
    private static final String DB_PASS = "pCLIsQwQVmsPiGw8";
    private static final String DB_URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/postgres";
    
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            connection.setAutoCommit(true);
            
            System.out.println("Berhasil terhubung ke Supabase PostgreSQL!");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver tidak ditemukan. Pastikan dependency sudah ter-load di Maven.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal koneksi ke Supabase. Cek URL, User, dan Password.", e);
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
}
