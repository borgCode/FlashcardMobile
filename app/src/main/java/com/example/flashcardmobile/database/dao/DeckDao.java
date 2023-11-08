package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.Deck;

import java.util.List;

@Dao
public interface DeckDao {
    
    @Insert
    void insert(Deck deck);
    @Update
    void update(Deck deck);
    @Delete
    void delete(Deck deck);
    @Query("SELECT * FROM decks")
    LiveData<List<Deck>> getAllDecks();
}
