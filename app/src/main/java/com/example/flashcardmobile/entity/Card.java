package com.example.flashcardmobile.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "cards",
        foreignKeys = @ForeignKey(entity = Deck.class,
                parentColumns = "id",
                childColumns = "deck_id",
                onDelete = ForeignKey.CASCADE))
public class Card {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "deck_id")
    private long deckId;
    @ColumnInfo(name = "front_side")
    private String frontSide;
    @ColumnInfo(name = "back_side")
    private String backSide;
    @ColumnInfo(name = "due_date")
    private LocalDateTime dueDate;
    private int interval;
    private int repetitions;
    private double easiness;


    public Card(long deckId, String frontSide, String backSide, LocalDateTime dueDate, int interval, int repetitions, double easiness) {
        this.deckId = deckId;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.dueDate = dueDate;
        this.interval = interval;
        this.repetitions = repetitions;
        this.easiness = easiness;
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

    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public double getEasiness() {
        return easiness;
    }

    public void setEasiness(double easiness) {
        this.easiness = easiness;
    }
}
