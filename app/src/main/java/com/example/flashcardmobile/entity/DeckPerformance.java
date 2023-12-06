package com.example.flashcardmobile.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "deck_performance",
        foreignKeys = @ForeignKey(entity = Deck.class,
                parentColumns = "id",
                childColumns = "deckId",
                onDelete = ForeignKey.CASCADE))
public class DeckPerformance {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long deckId;
    @ColumnInfo(name = "cards_studied")
    private int cardsStudied;
    @ColumnInfo(name = "easy_answers")
    private int easyAnswers;
    @ColumnInfo(name = "medium_answers")
    private int mediumAnswers;
    @ColumnInfo(name = "hard_answers")
    private int hardAnswers;

    public DeckPerformance(long deckId, int cardsStudied, int easyAnswers, int mediumAnswers, int hardAnswers) {
        this.deckId = deckId;
        this.cardsStudied = cardsStudied;
        this.easyAnswers = easyAnswers;
        this.mediumAnswers = mediumAnswers;
        this.hardAnswers = hardAnswers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public int getCardsStudied() {
        return cardsStudied;
    }

    public void setCardsStudied(int cardsStudied) {
        this.cardsStudied = cardsStudied;
    }

    public int getEasyAnswers() {
        return easyAnswers;
    }

    public void setEasyAnswers(int easyAnswers) {
        this.easyAnswers = easyAnswers;
    }

    public int getMediumAnswers() {
        return mediumAnswers;
    }

    public void setMediumAnswers(int mediumAnswers) {
        this.mediumAnswers = mediumAnswers;
    }

    public int getHardAnswers() {
        return hardAnswers;
    }

    public void setHardAnswers(int hardAnswers) {
        this.hardAnswers = hardAnswers;
    }
}
