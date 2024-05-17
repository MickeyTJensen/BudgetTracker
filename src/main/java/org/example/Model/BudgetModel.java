package org.example.Model;

import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Observer.BudgetObserver;
import org.example.Strategy.BudgetStrategy;

import java.util.*;

public class BudgetModel {
    private Map<IncomeType, Double> incomes = new EnumMap<>(IncomeType.class);
    private Map<ExpenseType, Double> expenses = new EnumMap<>(ExpenseType.class);
    private BudgetStrategy defaultStrategy;
    private BudgetStrategy taxStrategy;
    private List<BudgetObserver> observers = new ArrayList<>();

    // Konstruktor som tar emot två strategier
    public BudgetModel(BudgetStrategy defaultStrategy, BudgetStrategy taxStrategy) {
        this.defaultStrategy = defaultStrategy;
        this.taxStrategy = taxStrategy;
        initializeAccounts();
    }

    private void initializeAccounts() {
        for (IncomeType type : IncomeType.values()) {
            incomes.put(type, 0.0);
        }
        for (ExpenseType type : ExpenseType.values()) {
            expenses.put(type, 0.0);
        }
    }

    public void setIncome(IncomeType type, double amount) {
        double adjustedAmount = amount;
        if (type == IncomeType.LÖN) {
            adjustedAmount = taxStrategy.calculateBudget(Collections.singletonMap(type, amount), Collections.emptyMap());
        }
        incomes.put(type, adjustedAmount);
        notifyObservers();
    }

    public void setExpense(ExpenseType type, double amount) {
        expenses.put(type, amount);
        notifyObservers();
    }

    private void clearAll() {
        for (IncomeType type : IncomeType.values()) {
            incomes.put(type, 0.0);
        }
        for (ExpenseType type : ExpenseType.values()) {
            expenses.put(type, 0.0);
        }
    }

    public void addObserver(BudgetObserver observer) {
        observers.add(observer);
    }

    protected void notifyObservers() {
        for (BudgetObserver observer : observers) {
            observer.update(this);
        }
    }

    public double getTotalIncome() {

        return incomes.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getActualExpense(ExpenseType type) {

        return expenses.getOrDefault(type, 0.0);
    }

    public double getTotalExpenses() {

        return expenses.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public void updateExpenses(Map<ExpenseType, Double> updatedExpenses) {
        expenses.clear(); // Rensa nuvarande utgifter
        expenses.putAll(updatedExpenses); // Uppdatera med de nya optimerade utgifterna
        notifyObservers(); // Notifiera observerare om uppdateringen
    }

    public Map<ExpenseType, Double> getExpenses() {
        return new HashMap<>(expenses); // Returnerar en kopia för immutabilitet
    }

}
