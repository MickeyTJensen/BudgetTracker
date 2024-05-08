package org.example;

import org.example.Controller.BudgetController;
import org.example.Model.BudgetModel;
import org.example.Strategy.SimpleBudgetStrategy;
import org.example.View.BudgetView;

import javax.swing.*;

public class BudgetTracker {
    public static void main(String[] args) {
        Application app = new Application();
        app.start();
    }
}

