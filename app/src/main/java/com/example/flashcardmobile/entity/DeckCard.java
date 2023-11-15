package com.example.flashcardmobile.entity;

import java.time.LocalDateTime;

//Card with deck name included

public class DeckCard {
    private long deckId;
    private String deckName;
    private long cardId;
    private String frontSide;
    private String backSide;
    private LocalDateTime dueDate;

    public DeckCard(long deckId, String deckName, long cardId, String frontSide, String backSide, LocalDateTime dueDate) {
        this.deckId = deckId;
        this.deckName = deckName;
        this.cardId = cardId;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.dueDate = dueDate;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
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
}
