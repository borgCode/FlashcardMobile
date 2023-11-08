package com.example.flashcardmobile.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "decks")
public class Deck {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String deckName;


    public Deck(String deckName) {
        this.deckName = deckName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeckName() {
        return deckName;
    }
}
