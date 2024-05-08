package org.example.Model;

import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Observer.BudgetObserver;
import org.example.Strategy.BudgetStrategy;
import org.example.Strategy.SimpleBudgetStrategy;

import java.util.*;

public class BudgetModel {
    private Map<IncomeType, Double> incomes = new EnumMap<>(IncomeType.class);
    private Map<ExpenseType, Double> expenses = new EnumMap<>(ExpenseType.class);
    private BudgetStrategy strategy;

    public BudgetModel(BudgetStrategy strategy) {
        this.strategy = strategy;
        incomes = new HashMap<>();
        expenses = new HashMap<>();

        for (IncomeType type : IncomeType.values()) {
            incomes.put(type, 0.0);
        }
        for (ExpenseType type : ExpenseType.values()) {
            expenses.put(type, 0.0);
        }
    }

    public void setIncome(IncomeType type, double amount) {
        incomes.put(type, amount);
        notifyObservers();
    }

    public void setExpense(ExpenseType type, double amount) {
        expenses.put(type, amount);
        notifyObservers();
    }

    public Map<IncomeType, Double> getIncomes() {
        return incomes;
    }

    public Map<ExpenseType, Double> getExpenses() {
        return expenses;
    }

    public double calculateBudget() {
        return strategy.calculateBudget(incomes, expenses);
    }

    private List<BudgetObserver> observers = new ArrayList<>();

    public void addObserver(BudgetObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BudgetObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (BudgetObserver observer : observers) {
            observer.update(this);
        }
    }
    public void addIncome(IncomeType type, double amount) {
        incomes.put(type, incomes.getOrDefault(type, 0.0) + amount);
    }

    public double getTotalIncome() {
        return incomes.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public void calculateAndDistributeBudget() {
        double totalIncome = getTotalIncome();
        for (ExpenseType type : ExpenseType.values()) {
            expenses.put(type, totalIncome * type.getPercentage());
        }
    }
    public double getActualExpense(ExpenseType type) {
        return expenses.getOrDefault(type, 0.0);
    }

}
