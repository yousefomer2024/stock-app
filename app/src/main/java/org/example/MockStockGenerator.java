package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MockStockGenerator {
    private static final Random random = new Random();
    private static double lastPrice = 34000.00; // starting price for Dow Jones

    public static BigDecimal getPrice() {
        // simulate random fluctuations between -50 and +50
        double change = -50 + (100 * random.nextDouble());
        lastPrice += change;

        return BigDecimal.valueOf(lastPrice).setScale(2, RoundingMode.HALF_UP);
    }
}

