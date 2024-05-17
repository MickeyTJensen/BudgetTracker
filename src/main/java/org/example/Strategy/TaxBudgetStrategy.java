package org.example.Strategy;

import org.example.ExpenseType;
import org.example.IncomeType;

import java.util.Map;

public class TaxBudgetStrategy implements BudgetStrategy {
    private double taxRate = 0.30; // 20% skatt på inkomsten

    @Override
    public double calculateBudget(Map<IncomeType, Double> incomes, Map<ExpenseType, Double> expenses) {
        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        double taxedIncome = totalIncome;

        if (incomes.containsKey(IncomeType.LÖN)) {
            taxedIncome -= incomes.get(IncomeType.LÖN) * taxRate;
        }

        return taxedIncome - totalExpenses;
    }
}
