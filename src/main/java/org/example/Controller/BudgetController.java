package org.example.Controller;

import org.example.Algorithm.BudgetOptimizationAlgorithm;
import org.example.ExpenseType;
import org.example.IncomeType;
import org.example.Item.BudgetItem;
import org.example.Item.BudgetItemFactory;
import org.example.Model.BudgetModel;
import org.example.View.BudgetView;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class BudgetController {
    private BudgetModel model;
    private BudgetView view;
    private Map<ExpenseType, Double> originalExpenses; // Spara de ursprungliga utgifterna (nuvarande ekonomi)

    public BudgetController(BudgetModel model, BudgetView view) {
        this.model = model;
        setView(view);
        model.addObserver(view);
        initController();
        this.originalExpenses = new HashMap<>(model.getExpenses());
    }

    //Sätter vyn och kopplar kontrollern.
    public void setView(BudgetView view) {
        this.view = view;
        view.setController(this);
    }

    //Initialiserar kontrollern och lägger till lyssnare för inkomst- och utgiftsfält.
    private void initController() {
        view.getIncomeFields().forEach((type, field) -> {
            field.addActionListener(e -> handleItemCreation(type, field.getText(), true));
        });

        view.getExpenseFields().forEach((type, field) -> {
            field.addActionListener(e -> handleItemCreation(type, field.getText(), false));
        });
    }

    //Hanterar skapandet av budgetobjekt baserat på användarinmatning.
    private void handleItemCreation(Enum<?> type, String amountText, boolean isIncome) {
        try {
            double amount = Double.parseDouble(amountText);
            BudgetItem item = BudgetItemFactory.createBudgetItem(isIncome ? "income" : "expense");
            item.add(amount);

            if (isIncome) {
                model.setIncome((IncomeType) type, item.getAmount());
            } else {
                model.setExpense((ExpenseType) type, item.getAmount());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input for " + type.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Ger budgetråd baserat på nuvarande data
    public void adviceBudget() {
        double totalIncome = model.getTotalIncome();
        StringBuilder advice = new StringBuilder();
        advice.append("<html><b>Rådgivning:</b><br>");

        for (ExpenseType type : ExpenseType.values()) {
            double currentExpense = model.getActualExpense(type);
            double recommendedExpense = totalIncome * type.getPercentage();
            double percentageOfIncome = (currentExpense / totalIncome) * 100;
            double difference = recommendedExpense - currentExpense;

            if (type == ExpenseType.SPAR) {
                if (currentExpense < recommendedExpense) {
                    advice.append("<span style='color:red;'>")
                            .append("<br>")
                            .append(type.name())
                            .append(": Du sparar ")
                            .append(String.format("%.2f%%", percentageOfIncome))
                            .append(" av din inkomst på ")
                            .append(type.name().toLowerCase())
                            .append(", vilket är under den rekommenderade nivån på ")
                            .append(String.format("%.2f%%.", type.getPercentage() * 100))
                            .append(" Överväg att öka ditt sparande med ")
                            .append(String.format("%.2f kr", Math.abs(difference)))
                            .append(" för att nå rekommenderad nivå.")
                            .append("</span><br>");
                } else {
                    advice.append("<span style='color:green;'>")
                            .append("<br>")
                            .append(type.name())
                            .append(": Bra jobbat! Du sparar ")
                            .append(String.format("%.2f%%", percentageOfIncome))
                            .append(" av din inkomst på ")
                            .append(type.name().toLowerCase())
                            .append(", vilket är inom den rekommenderade nivån på ")
                            .append(String.format("%.2f%%.", type.getPercentage() * 100))
                            .append("</span><br>");
                }
            } else {
                if (percentageOfIncome > type.getPercentage() * 100) {
                    advice.append("<span style='color:red;'>")
                            .append("<br>")
                            .append(type.name())
                            .append(": Du spenderar ")
                            .append(String.format("%.2f%%", percentageOfIncome))
                            .append(" av din inkomst på ")
                            .append(type.name().toLowerCase())
                            .append(", vilket är över den rekommenderade nivån på ")
                            .append(String.format("%.2f%%.", type.getPercentage() * 100))
                            .append(" Överväg att minska denna kostnad med ")
                            .append(String.format("%.2f kr", Math.abs(difference)))
                            .append(" för att nå rekommenderad nivå.")
                            .append("</span><br>");
                } else {
                    advice.append("<span style='color:green;'>")
                            .append("<br>")
                            .append(type.name())
                            .append(": Bra jobbat! Du spenderar ")
                            .append(String.format("%.2f%%", percentageOfIncome))
                            .append(" av din inkomst på ")
                            .append(type.name().toLowerCase())
                            .append(", vilket är inom den rekommenderade nivån på ")
                            .append(String.format("%.2f%%.", type.getPercentage() * 100))
                            .append("</span><br>");
                }
            }
        }

        double totalExpenses = model.getTotalExpenses();
        double possibleSavings = totalIncome - (totalExpenses - model.getActualExpense(ExpenseType.SPAR));
        advice.append("<br>Din budget har ett underskott på: ").append(String.format("%.2f kr", possibleSavings)).append("<br>");
        advice.append("Se över dina abonnemang och onödiga utgifter.<br>");
        advice.append("</html>");
        view.updateAdvice(advice.toString());

        // Spara den ursprungliga budgeten
        originalExpenses = new HashMap<>(model.getExpenses());
        view.setOriginalExpenses(originalExpenses);
        view.setOptimizedView(false); // Visa endast nuvarande ekonomi pie chart
        view.updateCharts(model, originalExpenses, new HashMap<>()); // Uppdatera med ursprungliga data
    }

    //Optimerar budgeten baserat på nuvarande data och rekommendationer samt algoritm.
    public void optimizeBudget() {
        double totalIncome = model.getTotalIncome();
        Map<ExpenseType, Double> currentExpenses = new HashMap<>(originalExpenses); // Använd originalutgifter
        Map<ExpenseType, Double> optimizedExpenses = BudgetOptimizationAlgorithm.optimizeBudget(totalIncome, currentExpenses, ExpenseType.getRecommendedPercentages());

        // Beräkna totalt nuvarande sparande
        double currentSavings = currentExpenses.getOrDefault(ExpenseType.SPAR, 0.0);
        // Beräkna möjliga sparande med nuvarande utgifter
        double totalCurrentExpenses = currentExpenses.values().stream().mapToDouble(Double::doubleValue).sum();
        double possibleSavings = totalIncome - totalCurrentExpenses + currentSavings;

        model.updateExpenses(optimizedExpenses);

        StringBuilder advice = new StringBuilder("<html><b>Optimerad Budget Rådgivning:</b><br>");
        double totalSavingsGoal = totalIncome * 0.20; // Mål för sparande

        advice.append("Din totala inkomst: ").append(String.format("%.2f kr", totalIncome)).append("<br>");
        advice.append("Förslag på minsta sparande (20% av inkomsten): ").append(String.format("%.2f kr", totalSavingsGoal)).append("<br>");
        advice.append("Möjligt sparande med nuvarande utgifter: ").append(String.format("%.2f kr", possibleSavings)).append("<br><br>");

        StringBuilder actionAdvice = new StringBuilder("<br><b>Åtgärder:</b><br>");

        for (Map.Entry<ExpenseType, Double> entry : optimizedExpenses.entrySet()) {
            double currentExpense = originalExpenses.getOrDefault(entry.getKey(), 0.0); // Använd ifyllda utgifter
            double recommendedExpense = entry.getValue();
            double difference = recommendedExpense - currentExpense;
            String adjustment = difference > 0 ? "+" : "";

            advice.append(entry.getKey().name())
                    .append(": Aktuell utgift ")
                    .append(String.format("%.2f kr", currentExpense))
                    .append(", rekommenderad utgift ")
                    .append(String.format("%.2f kr", recommendedExpense))
                    .append(", justering ")
                    .append(adjustment)
                    .append(String.format("%.2f kr", difference))
                    .append(" (")
                    .append(String.format("%.2f%%", (currentExpense / totalIncome) * 100))
                    .append(")<br>");

            if (difference != 0) {
                switch (entry.getKey()) {
                    case TRANSPORT:
                        actionAdvice.append("TRANSPORT: Minska utgifterna genom att använda kollektivtrafik, cykla eller samåka. Justering ")
                                .append(adjustment)
                                .append(String.format("%.2f kr", difference))
                                .append(" för att uppnå maxnivån på 10% av inkomsten.<br>");
                        break;
                    case MAT:
                        actionAdvice.append("MAT: Planera dina måltider, handla smart och undvik att äta ute för ofta. Justering ")
                                .append(adjustment)
                                .append(String.format("%.2f kr", difference))
                                .append(" för att uppnå maxnivån på 20% av inkomsten.<br>");
                        break;
                    case NÖJEN:
                        actionAdvice.append("NÖJEN: Begränsa utgifter för nöje, överväg gratis aktiviteter och sätt en budget för dina nöjen. Justering ")
                                .append(adjustment)
                                .append(String.format("%.2f kr", difference))
                                .append(" för att uppnå maxnivån på 5% av inkomsten.<br>");
                        break;
                    default:
                        actionAdvice.append(entry.getKey().name()).append(": Justering ")
                                .append(adjustment)
                                .append(String.format("%.2f kr", difference))
                                .append(" för att uppnå en maxnivå på ")
                                .append(String.format("%.2f%%", (recommendedExpense / totalIncome) * 100))
                                .append(".<br>");
                        break;
                }
            }
        }

        advice.append(actionAdvice);
        advice.append("</html>");
        view.updateAdvice(advice.toString());

        // Uppdatera charts med både nuvarande och optimerad ekonomi
        view.setOptimizedView(true); // Visa både nuvarande och optimerad ekonomi pie charts
        view.updateCharts(model, originalExpenses, optimizedExpenses);
    }
}



