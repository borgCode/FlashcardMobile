package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.Card;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insert(Card card);
    @Update
    void update(Card card);
    @Delete
    void delete(Card card);
    @Query("SELECT * FROM cards WHERE deck_id = :deckId")
    LiveData<List<Card>> getCardsByDeckId(long deckId);
    @Query("SELECT * FROM cards WHERE deck_id = :deckId AND due_date <= :localTime")
    LiveData<List<Card>> getDueCardsByDeckId(long deckId, String localTime);
}
