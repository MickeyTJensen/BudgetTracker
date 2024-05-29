package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PieChart extends JPanel {

    // Konstruktor för PieChart som tar en titel, utgifter och totala utgifter
    public PieChart(String title, Map<ExpenseType, Double> expenses, double totalExpenses) {
        setLayout(new BorderLayout());

        // Skapa ett JFreeChart med angivna data
        JFreeChart pieChart = createChart(title, expenses != null ? expenses : new HashMap<>(), totalExpenses);

        // Lägg till diagrammet till panelen
        ChartPanel chartPanel = new ChartPanel(pieChart);
        add(chartPanel, BorderLayout.CENTER);
    }

    // Skapar ett JFreeChart baserat på utgifter och totala utgifter
    private JFreeChart createChart(String title, Map<ExpenseType, Double> expenses, double totalExpenses) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Lägg till varje utgiftstyp och dess procentandel till datasetet
        for (Map.Entry<ExpenseType, Double> entry : expenses.entrySet()) {
            double value = entry.getValue();
            double percentage = totalExpenses > 0 ? (value / totalExpenses) * 100 : 0;
            dataset.setValue(entry.getKey().name() + " (" + String.format("%.2f%%", percentage) + ")", value);
        }

        // Skapa ett pie chart med datasetet
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();

        // Sätt färger för varje sektion i pie chart
        plot.setSectionPaint("HYRA (40.00%)", Color.RED);
        plot.setSectionPaint("LÅN (10.00%)", Color.BLUE);
        plot.setSectionPaint("EL (10.00%)", Color.GREEN);
        plot.setSectionPaint("VATTEN (10.00%)", Color.YELLOW);
        plot.setSectionPaint("MAT (20.00%)", Color.ORANGE);
        plot.setSectionPaint("TRANSPORT (10.00%)", Color.PINK);
        plot.setSectionPaint("NÖJEN (5.00%)", Color.MAGENTA);
        plot.setSectionPaint("SPAR (20.00%)", Color.CYAN);
        return chart;
    }
}
