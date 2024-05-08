package org.example.Strategy;

import org.example.ExpenseType;
import org.example.IncomeType;

import java.util.Map;

public class SimpleBudgetStrategy implements BudgetStrategy {
    @Override
    public double calculateBudget(Map<IncomeType, Double> incomes, Map<ExpenseType, Double> expenses) {
        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        return totalIncome - totalExpenses;
    }
}