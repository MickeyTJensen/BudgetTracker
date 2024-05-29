package org.example.Strategy;

import org.example.ExpenseType;
import org.example.IncomeType;

import java.util.Map;

public class SimpleBudgetStrategy implements BudgetStrategy {
    @Override
    public double calculateBudget(Map<IncomeType, Double> incomes, Map<ExpenseType, Double> expenses) {
        // Beräknar den totala inkomsten genom att summera alla inkomster
        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();

        // Beräknar de totala utgifterna genom att summera alla utgifter
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();

        // Returnerar skillnaden mellan total inkomst och total utgift
        return totalIncome - totalExpenses;
    }
}
