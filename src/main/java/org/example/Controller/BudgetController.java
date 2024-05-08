package org.example.Controller;


import org.example.Application;
import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Model.BudgetModel;
import org.example.View.BudgetView;

import javax.swing.*;

public class BudgetController {
    private BudgetModel model;
    private BudgetView view;

    public BudgetController(BudgetModel model, BudgetView view) {
        this.model = model;
        this.view = view;
        model.addObserver(view);
        initController();
    }

    private void initController() {
        view.getIncomeFields().forEach((type, field) -> {
            field.addActionListener(e -> updateModel(type, field.getText(), true));
        });

        view.getExpenseFields().forEach((type, field) -> {
            field.addActionListener(e -> updateModel(type, field.getText(), false));
        });
    }

    private void updateModel(Enum<?> type, String text, boolean isIncome) {
        try {
            double value = Double.parseDouble(text);
            if (isIncome) {
                if (type instanceof IncomeType) {
                    model.setIncome((IncomeType) type, value);
                } else {
                    throw new IllegalArgumentException("Type mismatch: Expected IncomeType, got " + type.getClass());
                }
            } else {
                if (type instanceof ExpenseType) {
                    model.setExpense((ExpenseType) type, value);
                } else {
                    throw new IllegalArgumentException("Type mismatch: Expected ExpenseType, got " + type.getClass());
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input for " + type.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Type Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
