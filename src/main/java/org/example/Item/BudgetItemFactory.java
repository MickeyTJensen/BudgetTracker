package org.example.Item;

public class BudgetItemFactory {
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

