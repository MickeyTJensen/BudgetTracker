package org.example;

import java.util.HashMap;
import java.util.Map;

public enum ExpenseType {
    HYRA(0.40),
    LÅN(0.40),
    EL(0.10),
    VATTEN(0.10),
    MAT(0.20),
    TRANSPORT(0.10),
    NÖJEN(0.05),

    SPAR(0.20);

    private final double percentage;

    ExpenseType(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public static Map<ExpenseType, Double> getRecommendedPercentages() {
        Map<ExpenseType, Double> percentages = new HashMap<>();
        for (ExpenseType type : ExpenseType.values()) {
            percentages.put(type, type.getPercentage());
        }
        return percentages;
    }
}
