package com.example.flashcardmobile.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.TagDao;
import com.example.flashcardmobile.entity.CardWithTags;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.entity.TagWithCards;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TagRepository {
    private TagDao tagDao;
    private ExecutorService executorService;
    
    public TagRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        tagDao = database.tagDao();
    }

    public void insert(Tag tag) {
        Log.d("Tag R", "Inserting into db");
        CompletableFuture.runAsync(() -> tagDao.insert(tag), executorService);
    }

    public void update(Tag tag) {
        CompletableFuture.runAsync(() -> tagDao.update(tag), executorService);
    }

    public void delete(Tag tag) {
        CompletableFuture.runAsync(() -> tagDao.delete(tag), executorService);
    }
    
    public LiveData<List<Tag>> getAllTags() {
        return tagDao.getAllTags();
    }
    
    public LiveData<Tag> getTagById(long id) {
        return tagDao.getTagById(id);
    }
    
    public LiveData<List<TagWithCards>> getCardsForTag(long tagId) {
        return tagDao.getCardsForTag(tagId);
    }
    
    public LiveData<List<CardWithTags>> getTagsForCard(long cardId) {
        return tagDao.getTagsForCard(cardId);
    }
    
}
