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
    @Query("DELETE FROM decks WHERE id = :deckId")
    void deleteDeckById(long deckId);
    @Query("SELECT * FROM decks")
    LiveData<List<Deck>> getAllDecks();
    
    @Query("DELETE FROM decks")
    void deleteAllDecks();
}
