package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.example.flashcardmobile.entity.DeckCard;

import java.util.List;

@Dao
public interface DeckCardDao {
    @Query("SELECT decks.id AS deckId, decks.deck_name AS deckName," +
            " cards.id AS cardId, cards.front_side AS frontSide," +
            "cards.back_side AS backSide, cards.due_date AS dueDate " +
            "FROM decks JOIN cards ON decks.id = cards.deck_id")
    LiveData<List<DeckCard>> getAllCards();
}
