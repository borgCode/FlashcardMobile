package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.entity.DeckWithInfo;

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

    @Query("SELECT * FROM decks WHERE id = :id")
    LiveData<Deck> getDeckById(long id);
    
    @Transaction
    @Query("SELECT decks.*, (SELECT COUNT(*) FROM cards WHERE cards.deck_id = decks.id) as deckSize," +
            " (SELECT COUNT(*) FROM cards WHERE cards.deck_id = decks.id AND cards.due_date <= :now) as dueCardSize FROM decks")
    LiveData<List<DeckWithInfo>> getAllDecksWithInfo(String now);
}
