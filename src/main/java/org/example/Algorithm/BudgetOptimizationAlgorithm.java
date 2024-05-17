package org.example.Algorithm;

import org.example.ExpenseType;
import java.util.HashMap;
import java.util.Map;

public class BudgetOptimizationAlgorithm {
    public static Map<ExpenseType, Double> optimizeBudget(double totalIncome, Map<ExpenseType, Double> currentExpenses, Map<ExpenseType, Double> recommendedPercentages) {
        Map<ExpenseType, Double> optimizedBudget = new HashMap<>();

        for (ExpenseType type : ExpenseType.values()) {
            double recommendedExpense = totalIncome * recommendedPercentages.getOrDefault(type, 0.0);
            double actualExpense = currentExpenses.getOrDefault(type, 0.0);

            if (type == ExpenseType.HYRA || type == ExpenseType.LÅN) {
                optimizedBudget.put(type, actualExpense);
            } else {
                if (actualExpense > recommendedExpense) {
                    optimizedBudget.put(type, recommendedExpense);
                } else {
                    optimizedBudget.put(type, actualExpense);
                }
            }
        }

        // Ensure at least 20% savings
        double currentSavings = currentExpenses.getOrDefault(ExpenseType.SPAR, 0.0);
        double recommendedSavings = totalIncome * recommendedPercentages.getOrDefault(ExpenseType.SPAR, 0.0);
        if (currentSavings < recommendedSavings) {
            optimizedBudget.put(ExpenseType.SPAR, recommendedSavings);
        }

        // Adjust other expenses to ensure total expenses do not exceed total income
        double totalExpenses = optimizedBudget.values().stream().mapToDouble(Double::doubleValue).sum();
        while (totalExpenses > totalIncome) {
            for (ExpenseType type : ExpenseType.values()) {
                if (type != ExpenseType.HYRA && type != ExpenseType.LÅN && type != ExpenseType.SPAR) {
                    double currentOptimizedExpense = optimizedBudget.getOrDefault(type, 0.0);
                    if (currentOptimizedExpense > 0) {
                        double reducedExpense = currentOptimizedExpense * 0.95; // Reduce by 5%
                        optimizedBudget.put(type, reducedExpense);
                        totalExpenses = optimizedBudget.values().stream().mapToDouble(Double::doubleValue).sum();
                        if (totalExpenses <= totalIncome) {
                            break;
                        }
                    }
                }
            }
        }

        return optimizedBudget;
    }
}






