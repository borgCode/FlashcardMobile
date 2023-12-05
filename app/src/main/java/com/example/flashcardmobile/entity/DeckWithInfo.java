package com.example.flashcardmobile.entity;

import androidx.room.Embedded;

public class DeckWithInfo {
    @Embedded
    public Deck deck;
    
    public int deckSize;
    public int dueCardSize;

    public DeckWithInfo(Deck deck, int deckSize, int dueCardSize) {
        this.deck = deck;
        this.deckSize = deckSize;
        this.dueCardSize = dueCardSize;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public int getDueCardSize() {
        return dueCardSize;
    }

    public void setDueCardSize(int dueCardSize) {
        this.dueCardSize = dueCardSize;
    }
}
