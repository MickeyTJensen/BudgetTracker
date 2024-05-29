package org.example.Item;

public interface BudgetItem {
    // Lägger till ett belopp till budgetposten
    void add(double amount);

    // Hämtar det totala beloppet för budgetposten
    double getAmount();
}
