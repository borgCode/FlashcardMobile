package com.example.flashcardmobile.viewmodel;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

public class SharedAnalyticsViewModel extends ViewModel {
    
    private LocalDate date;
    private int cardsStudied;
    private int cardsAdded;
    private int cardsMastered;

    public SharedAnalyticsViewModel() {
        this.date = LocalDate.now();
    }
    public void updateStudiedCards(int count) {
        this.studiedCards += count;
        // Add logic to persist data to the database
    }

    public void updateCardsAdded(int count) {
        this.cardsAdded += count;
        // Persist to database
    }

    public void updateCardsMastered(int count) {
        this.cardsMastered += count;
        // Persist to database
    }
    
    
}
