package com.example.flashcardmobile.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity
public class LearningAnalytics {
    
    @PrimaryKey
    private LocalDate date;
    @ColumnInfo(name = "cards_studied")
    private int cardsStudied;
    @ColumnInfo(name = "cards_added")
    private int cardsAdded;
    @ColumnInfo(name = "cards_mastered")
    private int cardsMastered;

    public LearningAnalytics(LocalDate date, int cardsStudied, int cardsAdded, int cardsMastered) {
        this.date = date;
        this.cardsStudied = cardsStudied;
        this.cardsAdded = cardsAdded;
        this.cardsMastered = cardsMastered;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCardsStudied() {
        return cardsStudied;
    }

    public void setCardsStudied(int cardsStudied) {
        this.cardsStudied = cardsStudied;
    }

    public int getCardsAdded() {
        return cardsAdded;
    }

    public void setCardsAdded(int cardsAdded) {
        this.cardsAdded = cardsAdded;
    }

    public int getCardsMastered() {
        return cardsMastered;
    }

    public void setCardsMastered(int cardsMastered) {
        this.cardsMastered = cardsMastered;
    }
}
