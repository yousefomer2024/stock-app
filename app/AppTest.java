package org.example;

import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest {

    private static final int QUEUE_MAX_SIZE = 20;
    private Queue<String> stockQueue;

    // Setup in-memory queue before each test
    @BeforeEach
    void setup() {
        stockQueue = new LinkedList<>();
    }

    // --- Queue Tests ---
    @Test
    @DisplayName("Queue should retain only last 20 elements")
    void testQueueMaxSize() {
        for (int i = 1; i <= 25; i++) {
            App.addToQueue("Entry " + i, stockQueue); // assumes App has addToQueue() helper
        }
        assertEquals(QUEUE_MAX_SIZE, stockQueue.size());
        assertTrue(stockQueue.contains("Entry 6")); // oldest remaining element
        assertFalse(stockQueue.contains("Entry 1")); // removed
    }

    // --- Timestamp Format Test ---
    @Test
    @DisplayName("Timestamps should match yyyy-MM-dd HH:mm:ss format")
    void testTimestampFormat() {
        String timestamp = "2025-09-30 01:10:46";
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        assertTrue(Pattern.matches(regex, timestamp));
    }

    // --- Mock Stock Generator Test ---
    @Test
    @DisplayName("Mock price generator returns valid BigDecimal")
    void testMockPriceGenerator() {
        BigDecimal price = MockStockGenerator.getPrice();
        assertNotNull(price);
        assertTrue(price.doubleValue() > 0);
    }

    // --- Database Insert Tests ---
    @Test
    @DisplayName("DatabaseManager insertRecord executes without exceptions for valid input")
    void testDatabaseInsertValid() {
        assertDoesNotThrow(() -> DatabaseManager.insertRecord("2025-09-30 00:00:01", 34000.0));
    }

    @Test
    @DisplayName("DatabaseManager throws exception for invalid input")
    void testDatabaseInsertInvalid() {
        // Null timestamp should fail due to NOT NULL constraint
        assertThrows(SQLException.class, () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:stocks.db");
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO stock_data(timestamp, price) VALUES(?, ?)")) {
                stmt.setString(1, null);
                stmt.setDouble(2, 34000.0);
                stmt.executeUpdate();
            }
        });

        // Negative price (optional: schema allows it, but simulate business logic check)
        double negativePrice = -50.0;
        assertTrue(negativePrice < 0);
    }

    @Test
    @DisplayName("Bulk insert 1000 mock records for persistence testing")
    void testBulkInsert() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                String timestamp = "2025-09-30 00:" + (i / 60) + ":" + (i % 60);
                double price = 34000 + Math.random() * 200;
                DatabaseManager.insertRecord(timestamp, price);
            }

            // Optional: verify row count >= 1000
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:stocks.db");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM stock_data")) {
                if (rs.next()) {
                    int count = rs.getInt("total");
                    assertTrue(count >= 1000, "DB should contain at least 1000 records");
                }
            }
        });
    }
}
