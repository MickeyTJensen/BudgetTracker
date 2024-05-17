package org.example.Item;

public class ExpenseItem implements BudgetItem {
    private double amount = 0;

    @Override
    public void add(double amount) {
        this.amount += amount;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }
}
