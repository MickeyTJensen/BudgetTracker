package org.example.View;


import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Model.BudgetModel;
import org.example.Observer.BudgetObserver;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import java.util.Observable;

public class BudgetView implements BudgetObserver {
    private JPanel mainPanel;

    private JPanel incomePanel;
    private JPanel expensePanel;
    private JPanel summaryPanel;
    private JPanel advicePanel;
    private JTextPane summaryTextPane;
    private JTextPane adviceTextPane;

    private Map<IncomeType, JTextField> incomeFields = new HashMap<>();
    private Map<ExpenseType, JTextField> expenseFields = new HashMap<>();

    private BudgetModel model;

    public BudgetView(BudgetModel model) {
        this.model = model;
        model.addObserver(this);
        initializeUI();
    }

    private void initializeUI() {
        mainPanel = new JPanel(new GridLayout(2, 2));

        incomePanel = createInputPanel(IncomeType.class, incomeFields, "Inkomster");
        expensePanel = createInputPanel(ExpenseType.class, expenseFields, "Utgifter");

        summaryPanel = createSummaryPanel();
        advicePanel = createAdvicePanel();

        mainPanel.add(incomePanel);
        mainPanel.add(summaryPanel);
        mainPanel.add(expensePanel);
        mainPanel.add(advicePanel);
    }

    private <E extends Enum<E>> JPanel createInputPanel(Class<E> enumClass, Map<E, JTextField> map, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        for (E type : enumClass.getEnumConstants()) {
            JLabel label = new JLabel(type.name());
            JTextField textField = new JTextField(10);
            textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
            panel.add(label);
            panel.add(textField);
            map.put(type, textField);
        }

        return panel;
    }




    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Sammanfattning"), BorderLayout.NORTH);

        summaryTextPane = new JTextPane();
        summaryTextPane.setContentType("text/html");  // JTextPane stöder HTML
        summaryTextPane.setEditable(false);
        panel.add(new JScrollPane(summaryTextPane), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAdvicePanel() {
        advicePanel = new JPanel(new BorderLayout());
        advicePanel.add(new JLabel("Rådgivning"), BorderLayout.NORTH);

        adviceTextPane = new JTextPane();
        adviceTextPane.setContentType("text/html");  // JTextPane stöder HTML
        adviceTextPane.setEditable(false);
        advicePanel.add(new JScrollPane(adviceTextPane), BorderLayout.CENTER);
        return advicePanel;
    }

    @Override
    public void update(BudgetModel model) {
        SwingUtilities.invokeLater(() -> {
            double totalIncomes = model.getTotalIncome();
            StringBuilder summary = new StringBuilder("\n<html><b>Sammanfattning:</b><br>");
            StringBuilder advice = new StringBuilder("<html><b>Rådgivning:</b><br>");

            summary.append("\nTotal Inkomster: ").append(String.format("%.2f", totalIncomes)).append("<br>");

            for (ExpenseType type : ExpenseType.values()) {
                double recommendedExpense = totalIncomes * type.getPercentage();
                double actualExpense = model.getActualExpense(type);
                summary.append("\nTotala Utgifter: \n").append(String.format("%.2f", totalIncomes)).append("<br>") + (type.name()).append(": ").append(String.format("%.2f", actualExpense)).append("<br>");

                if (actualExpense > recommendedExpense) {
                    advice.append("<span style='color:red;'>").append(type.name()).append(": Över budget med ")
                            .append(String.format("%.2f", actualExpense - recommendedExpense))
                            .append("</span><br>");
                } else {
                    advice.append("<span style='color:green;'>").append(type.name()).append(": Inom budget med ")
                            .append(String.format("%.2f", recommendedExpense - actualExpense))
                            .append("</span><br>");
                }
            }

            summary.append("</html>");
            advice.append("</html>");

            summaryTextPane.setText(summary.toString());
            adviceTextPane.setText(advice.toString());
        });
    }




    public Map<IncomeType, JTextField> getIncomeFields() {
        return incomeFields;
    }

    public Map<ExpenseType, JTextField> getExpenseFields() {
        return expenseFields;
    }

}


