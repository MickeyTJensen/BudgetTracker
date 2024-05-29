package org.example.Item;

public class BudgetItemFactory {
    // Skapar ett BudgetItem-objekt baserat på typen (income eller expense)
    public static BudgetItem createBudgetItem(String type) {
        if (type.equalsIgnoreCase("income")) {
            return new IncomeItem();
        } else if (type.equalsIgnoreCase("expense")) {
            return new ExpenseItem();
        } else {
            throw new IllegalArgumentException("Unknown budget item type");
        }
    }
}


