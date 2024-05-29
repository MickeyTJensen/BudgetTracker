package org.example.Model;

import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Observer.BudgetObserver;
import org.example.Strategy.BudgetStrategy;

import java.util.*;

public class BudgetModel {
    private Map<IncomeType, Double> incomes = new EnumMap<>(IncomeType.class); // Håller alla inkomster
    private Map<ExpenseType, Double> expenses = new EnumMap<>(ExpenseType.class); // Håller alla utgifter
    private BudgetStrategy defaultStrategy; // Standardbudgetstrategi
    private BudgetStrategy taxStrategy; // Budgetstrategi som tar hänsyn till skatt
    private List<BudgetObserver> observers = new ArrayList<>(); // Lista över observatörer

    // Konstruktor som tar emot två strategier
    public BudgetModel(BudgetStrategy defaultStrategy, BudgetStrategy taxStrategy) {
        this.defaultStrategy = defaultStrategy;
        this.taxStrategy = taxStrategy;
        initializeAccounts();
    }

    // Initialiserar konton med standardvärden
    private void initializeAccounts() {
        for (IncomeType type : IncomeType.values()) {
            incomes.put(type, 0.0);
        }
        for (ExpenseType type : ExpenseType.values()) {
            expenses.put(type, 0.0);
        }
    }

    // Sätter inkomstbelopp för en specifik typ
    public void setIncome(IncomeType type, double amount) {
        double adjustedAmount = amount;
        if (type == IncomeType.LÖN) {
            adjustedAmount = taxStrategy.calculateBudget(Collections.singletonMap(type, amount), Collections.emptyMap());
        }
        incomes.put(type, adjustedAmount);
        notifyObservers();
    }

    // Sätter utgiftsbelopp för en specifik typ
    public void setExpense(ExpenseType type, double amount) {
        expenses.put(type, amount);
        notifyObservers();
    }

    // Rensar alla konton
    private void clearAll() {
        for (IncomeType type : IncomeType.values()) {
            incomes.put(type, 0.0);
        }
        for (ExpenseType type : ExpenseType.values()) {
            expenses.put(type, 0.0);
        }
    }

    // Lägger till en observatör
    public void addObserver(BudgetObserver observer) {
        observers.add(observer);
    }

    // Notifierar alla observatörer om en uppdatering
    protected void notifyObservers() {
        for (BudgetObserver observer : observers) {
            observer.update(this);
        }
    }

    // Hämtar den totala inkomsten
    public double getTotalIncome() {
        return incomes.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Hämtar den faktiska utgiften för en specifik typ
    public double getActualExpense(ExpenseType type) {
        return expenses.getOrDefault(type, 0.0);
    }

    // Hämtar de totala utgifterna
    public double getTotalExpenses() {
        return expenses.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Uppdaterar utgifterna med nya värden
    public void updateExpenses(Map<ExpenseType, Double> updatedExpenses) {
        expenses.clear(); // Rensa nuvarande utgifter
        expenses.putAll(updatedExpenses); // Uppdatera med de nya optimerade utgifterna
        notifyObservers(); // Notifiera observatörer om uppdateringen
    }

    // Hämtar en kopia av utgifterna
    public Map<ExpenseType, Double> getExpenses() {
        return new HashMap<>(expenses); // Returnerar en kopia för immutabilitet
    }
}

