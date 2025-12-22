package com.craftstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// =================================================
// VERİTABANI BAĞLANTI SINIFI
// Tüm DAO sınıfları buradan Connection alır
// =================================================
public class DatabaseConnection {

    // PostgreSQL bağlantı bilgileri (demo amaçlı)
    private static final String URL = "jdbc:postgresql://localhost:5432/craftstore_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Lcsn8319";

    // Bağlantı oluşturan yardımcı metot
    public static Connection connect() {
        try {
            // DriverManager üzerinden bağlantı alınır
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Bağlantı hatası konsola yazdırılır
            System.out.println("DB bağlantı hatası: " + e.getMessage());
            return null;
        }
    }
}
