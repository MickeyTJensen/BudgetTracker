package org.example.Item;

public class IncomeItem implements BudgetItem {
    private double amount = 0; // Håller det totala beloppet för inkomsten

    @Override
    public void add(double amount) {
        // Lägger till ett belopp till inkomsten
        this.amount += amount;
    }

    @Override
    public double getAmount() {
        // Returnerar det totala beloppet för inkomsten
        return this.amount;
    }
}

