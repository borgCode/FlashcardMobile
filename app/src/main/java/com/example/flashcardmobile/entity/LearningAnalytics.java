package com.example.flashcardmobile.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "learning_analytics")
public class LearningAnalytics {

    @PrimaryKey
    @NonNull
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
