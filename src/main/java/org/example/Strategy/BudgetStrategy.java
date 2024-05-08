package org.example.Strategy;

import org.example.ExpenseType;
import org.example.IncomeType;

import java.util.Map;

public interface BudgetStrategy {
    double calculateBudget(Map<IncomeType, Double> incomes, Map<ExpenseType, Double> expenses);
}
