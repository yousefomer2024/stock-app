package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.File;

public class DatabaseManager {

    // Absolute path to project root DB
    private static final String DB_PATH = "/Users/yousefomer/stock-app/stocks.db";
    private static final String DB_URL = "jdbc:sqlite:stocks.db";



    // Create table if not exists
    static {
        System.out.println("SQLite DB will be created/used at: " + DB_PATH);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS stock_data (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "timestamp TEXT NOT NULL, " +
                             "price REAL NOT NULL)"
             )) {
            stmt.executeUpdate();
            System.out.println("Database table 'stock_data' is ready.");
        } catch (SQLException e) {
            System.err.println("Error initializing DB: " + e.getMessage());
        }
    }

    // Insert row into DB
    public static void insertRecord(String timestamp, double price) {
        String sql = "INSERT INTO stock_data(timestamp, price) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, timestamp);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();

            // Print confirmation
            System.out.println("Inserted into DB: " + timestamp + " | " + price);

        } catch (SQLException e) {
            System.err.println("DB Insert Error: " + e.getMessage());
        }
    }
}

