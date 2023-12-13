package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.*;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);
    @Update
    void update(Tag tag);
    @Delete
    void delete(Tag tag);
    @Query("SELECT * FROM tags")
    LiveData<List<Tag>> getAllTags();
    @Query("SELECT tags.id, tags.tag_name, tags.color, COUNT(card_tag_cross_ref.cardId) as cardCount " +
            "FROM tags " +
            "LEFT JOIN card_tag_cross_ref ON tags.id = card_tag_cross_ref.tagId " +
            "GROUP BY tags.id")
    LiveData<List<TagWithCardCount>> getTagsWithCardCounts();
    @Query("SELECT * FROM tags WHERE id = :id")
    LiveData<Tag> getTagById(long id);
    
    @Transaction
    @Query("SELECT * FROM tags WHERE id = :tagId")
    LiveData<TagWithCards> getCardsForTag(long tagId);

    @Transaction
    @Query("SELECT * FROM cards WHERE id = :cardId")
    LiveData<CardWithTags> getTagsForCard(long cardId);
    
    @Transaction
    @Insert
    void insertCrossRefs (List<CardTagCrossRef> cardTagCrossRefs);
    
    @Transaction
    default void updateCrossRefs(long cardId, List<CardTagCrossRef> crossRefs) {
        deleteCrossRefsByCardId(cardId);
        insertCrossRefs(crossRefs);
    }
    @Query("DELETE FROM card_tag_cross_ref WHERE cardId =:cardId")
    void deleteCrossRefsByCardId(long cardId);
}
