package org.example.View;

import org.example.Controller.BudgetController;
import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Model.BudgetModel;
import org.example.Observer.BudgetObserver;
import org.example.PieChart;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BudgetView implements BudgetObserver {
    private JPanel mainPanel;
    private JPanel incomePanel;
    private JPanel expensePanel;
    private JPanel summaryPanel;
    private JPanel advicePanel;
    private JPanel chartPanel;
    private JTextPane summaryTextPane;
    private JTextPane adviceTextPane;
    private JButton adviceButton;
    private JButton optimizeButton;
    private BudgetController controller;
    private Map<IncomeType, JTextField> incomeFields = new HashMap<>();
    private Map<ExpenseType, JTextField> expenseFields = new HashMap<>();
    private BudgetModel model;
    private boolean isOptimizedView = false; // Flagga för att indikera om vi visar den optimerade vyn
    private Map<ExpenseType, Double> originalExpenses; // Sparar de ursprungliga utgifterna

    // Konstruktor som initialiserar modellen, kontrollern och användargränssnittet
    public BudgetView(BudgetModel model, BudgetController controller) {
        this.model = model;
        this.controller = controller;
        model.addObserver(this); // Lägg till denna vy som observatör av modellen
        initializeUI(); // Initiera användargränssnittet
    }

    // Initialiserar användargränssnittet
    private void initializeUI() {
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Skapa och lägg till incomePanel
        incomePanel = createInputPanel(IncomeType.class, incomeFields, "Inkomster");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        mainPanel.add(incomePanel, gbc);

        // Skapa och lägg till summaryPanel
        summaryPanel = createSummaryPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        mainPanel.add(summaryPanel, gbc);

        // Skapa och lägg till expensePanel
        expensePanel = createInputPanel(ExpenseType.class, expenseFields, "Utgifter");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        mainPanel.add(expensePanel, gbc);

        // Skapa och lägg till advicePanel
        advicePanel = createAdvicePanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 0.25;
        mainPanel.add(advicePanel, gbc);

        // Skapa och lägg till chartPanel
        chartPanel = createChartPanel();
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 0.25;
        mainPanel.add(chartPanel, gbc);
    }

    // Skapar panelen för sammanfattning
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Sammanfattning"), BorderLayout.NORTH);

        summaryTextPane = new JTextPane();
        summaryTextPane.setContentType("text/html");
        summaryTextPane.setEditable(false);
        panel.add(new JScrollPane(summaryTextPane), BorderLayout.CENTER);

        adviceButton = new JButton("Rådgivning");
        adviceButton.addActionListener(e -> controller.adviceBudget());

        panel.add(adviceButton, BorderLayout.SOUTH);

        return panel;
    }

    // Skapar panelen för rådgivning
    private JPanel createAdvicePanel() {
        advicePanel = new JPanel(new BorderLayout());
        advicePanel.add(new JLabel("Rådgivning"), BorderLayout.NORTH);

        adviceTextPane = new JTextPane();
        adviceTextPane.setContentType("text/html");
        adviceTextPane.setEditable(false);
        adviceTextPane.setText("<html><b>Rådgivning:</b><br>Tryck på 'Rådgivning' för rådgivning angående din nuvarande budget.</html>");
        advicePanel.add(new JScrollPane(adviceTextPane), BorderLayout.CENTER);

        optimizeButton = new JButton("Optimera");
        optimizeButton.addActionListener(e -> controller.optimizeBudget());

        advicePanel.add(optimizeButton, BorderLayout.SOUTH);

        return advicePanel;
    }

    // Skapar panelen för inmatning
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

    // Skapar panelen för diagram
    private JPanel createChartPanel() {
        chartPanel = new JPanel(new GridLayout(1, 2));
        return chartPanel;
    }

    // Uppdaterar diagrammen med de ursprungliga och optimerade utgifterna
    public void updateCharts(BudgetModel model, Map<ExpenseType, Double> originalExpenses, Map<ExpenseType, Double> optimizedExpenses) {
        chartPanel.removeAll();

        double totalIncome = model.getTotalIncome();

        // Skapar diagram för nuvarande ekonomi
        PieChart currentChart = new PieChart("Nuvarande Ekonomi", originalExpenses != null ? originalExpenses : new HashMap<>(), totalIncome);
        chartPanel.add(currentChart);

        // Skapar diagram för optimerad ekonomi om flaggan är satt
        if (isOptimizedView) {
            PieChart optimizedChart = new PieChart("Optimerad Ekonomi", optimizedExpenses != null ? optimizedExpenses : new HashMap<>(), totalIncome);
            chartPanel.add(optimizedChart);
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // Uppdaterar vyn när modellen ändras
    @Override
    public void update(BudgetModel model) {
        SwingUtilities.invokeLater(() -> {
            double totalIncomes = model.getTotalIncome();
            double totalExpenses = model.getTotalExpenses();
            StringBuilder summary = new StringBuilder("<html><b>Sammanfattning:</b><br>");
            StringBuilder advice = new StringBuilder("<html><b>Rådgivning:</b><br>");

            summary.append("Totala Inkomster (efter skatt): ").append(String.format("%.2f", totalIncomes)).append(" kr<br>");
            summary.append("Totala Utgifter: ").append(String.format("%.2f", totalExpenses)).append(" kr<br>");
            for (ExpenseType type : ExpenseType.values()) {
                double actualExpense = model.getActualExpense(type);
                summary.append(type.name()).append(": ").append(String.format("%.2f", actualExpense)).append(" kr<br>");
            }
            summary.append("</html>");
            advice.append("</html>");

            summaryTextPane.setText(summary.toString());
            adviceTextPane.setText(advice.toString());

            // Uppdatera diagram med ursprungliga utgifter om inte optimerad vy
            if (!isOptimizedView) {
                updateCharts(model, originalExpenses, new HashMap<>()); // Tom karta för optimerade utgifter
            }
        });
    }

    // Uppdaterar rådgivningstexten
    public void updateAdvice(String adviceHtml) {
        SwingUtilities.invokeLater(() -> {
            adviceTextPane.setText(adviceHtml);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setController(BudgetController controller) {
        this.controller = controller;
    }

    public Map<IncomeType, JTextField> getIncomeFields() {
        return incomeFields;
    }

    public Map<ExpenseType, JTextField> getExpenseFields() {
        return expenseFields;
    }

    public void setOptimizedView(boolean optimizedView) {
        isOptimizedView = optimizedView;
    }

    public void setOriginalExpenses(Map<ExpenseType, Double> originalExpenses) {
        this.originalExpenses = originalExpenses;
    }

    public Map<ExpenseType, Double> getOriginalExpenses() {
        return originalExpenses;
    }
}



