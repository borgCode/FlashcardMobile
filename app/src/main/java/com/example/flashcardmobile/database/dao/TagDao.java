package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.CardTagCrossRef;
import com.example.flashcardmobile.entity.CardWithTags;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.entity.TagWithCards;

import java.util.List;

public interface TagDao {
    @Insert
    void insert(Tag tag);
    @Update
    void update(Tag tag);
    @Delete
    void delete(Tag tag);
    @Query("SELECT * FROM tags")
    LiveData<List<Tag>> getAllTags();
    @Query("SELECT * FROM tags WHERE id = :id")
    LiveData<Tag> getTagById(long id);
    
    @Transaction
    @Query("SELECT * FROM tags WHERE id = :tagId")
    LiveData<List<TagWithCards>> getCardsForTag(long tagId);

    @Transaction
    @Query("SELECT * FROM cards WHERE id = :cardId")
    LiveData<List<CardWithTags>> getTagsForCard(long cardId);
    
    
}
