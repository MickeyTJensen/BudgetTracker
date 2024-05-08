package org.example;

public enum ExpenseType {
    RENT(0.20),
    UTILITIES(0.20),
    FOOD(0.10),
    TRANSPORTATION(0.10),
    ENTERTAINMENT(0.05),
    OTHER(0.05);

    private final double percentage;

    ExpenseType(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
