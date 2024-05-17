package org.example.Strategy;

import org.example.ExpenseType;
import org.example.IncomeType;

import java.util.Map;

public class TaxBudgetStrategy implements BudgetStrategy {
    private double taxRate = 0.30; // 30% skatt på inkomsten

    @Override
    public double calculateBudget(Map<IncomeType, Double> incomes, Map<ExpenseType, Double> expenses) {
        // Beräknar den totala inkomsten genom att summera alla inkomster
        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();

        // Beräknar de totala utgifterna genom att summera alla utgifter
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();

        double taxedIncome = totalIncome;

        // Om inkomsttypen LÖN finns, dras skatt från denna inkomst
        if (incomes.containsKey(IncomeType.LÖN)) {
            taxedIncome -= incomes.get(IncomeType.LÖN) * taxRate;
        }

        // Returnerar den beskattade inkomsten minus de totala utgifterna
        return taxedIncome - totalExpenses;
    }
}

