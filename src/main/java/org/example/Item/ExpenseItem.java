package org.example.Item;

public class ExpenseItem implements BudgetItem {
    private double amount = 0; // Håller det totala beloppet för utgiften

    @Override
    public void add(double amount) {
        // Lägger till ett belopp till utgiften
        this.amount += amount;
    }

    @Override
    public double getAmount() {
        // Returnerar det totala beloppet för utgiften
        return this.amount;
    }
}
