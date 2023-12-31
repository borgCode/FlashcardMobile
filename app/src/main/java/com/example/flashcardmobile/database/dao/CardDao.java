package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.Card;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    long insert(Card card);
    @Update
    void update(Card card);
    @Delete
    void delete(Card card);
    @Query("DELETE FROM cards WHERE id = :id")
    void deleteById(long id);
    @Query("SELECT * FROM cards WHERE deck_id = :deckId")
    LiveData<List<Card>> getCardsByDeckId(long deckId);
    @Query("SELECT * FROM cards WHERE deck_id = :deckId AND due_date <= :localTime")
    LiveData<List<Card>> getDueCardsByDeckId(long deckId, String localTime);

    @Query("SELECT * FROM cards WHERE id = :id")
    LiveData<Card> getCardById(long id);

    
}
