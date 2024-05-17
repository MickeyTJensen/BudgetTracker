package org.example;

import org.example.Controller.BudgetController;
import org.example.Model.BudgetModel;
import org.example.Strategy.BudgetStrategy;
import org.example.Strategy.SimpleBudgetStrategy;
import org.example.Strategy.TaxBudgetStrategy;
import org.example.View.BudgetView;

import javax.swing.*;
import java.awt.*;

public class Application {
    private JFrame frame;
    private BudgetModel model;
    private BudgetView view;
    private BudgetController controller;

    public Application() {
        // Skapa strategierna
        BudgetStrategy defaultStrategy = new SimpleBudgetStrategy();
        BudgetStrategy taxStrategy = new TaxBudgetStrategy();

        // Skapa modellen med två strategier
        model = new BudgetModel(defaultStrategy, taxStrategy);

        // Skapa vyn innan du skapar kontrollern
        view = new BudgetView(model, null);  // Tillfälligt sätt controller till null

        // Skapa kontrollern med referens till vyn
        controller = new BudgetController(model, view);

        // Nu när kontrollern är skapad, sätt den i vyn
        view.setController(controller);

        // Förbered och visa fönstret
        frame = new JFrame("Budget Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1200);
        frame.setContentPane(view.getMainPanel());
        frame.setVisible(true);
    }

    public void start() {
        // Sätt fönstret till mitten av skärmen och visa det
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Starta applikationen
        new Application().start();
    }
}
