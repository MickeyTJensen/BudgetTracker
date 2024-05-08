package org.example;

import org.example.Controller.BudgetController;
import org.example.Model.BudgetModel;
import org.example.Strategy.BudgetStrategy;
import org.example.Strategy.SimpleBudgetStrategy;
import org.example.View.BudgetView;

import javax.swing.*;
import java.awt.*;

public class Application {
    private BudgetModel model;
    private BudgetView view;
    private BudgetController controller;
    private JFrame frame;

    public Application() {
        model = new BudgetModel(new SimpleBudgetStrategy());
        view = new BudgetView(model);
        controller = new BudgetController(model, view);

        frame = new JFrame("Budget Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setContentPane(view.getMainPanel());
        frame.setVisible(true);
    }

    public void start() {
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Application().start();
    }
}

