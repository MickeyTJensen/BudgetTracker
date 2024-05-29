package org.example.Observer;

import org.example.Model.BudgetModel;


public interface BudgetObserver {
    // Metod som kallas när modellen uppdateras
    void update(BudgetModel model);
}

