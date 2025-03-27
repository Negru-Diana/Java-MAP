package com.example.examen_db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/examen_practic_MAP";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    // Metoda pentru obtinerea conexiunii
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Eroare la conectarea la baza de date: " + e.getMessage());
            throw e; // Arunca din nou exceptia
        }
    }
}
