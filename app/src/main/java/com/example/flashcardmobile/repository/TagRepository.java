package com.example.flashcardmobile.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.TagDao;
import com.example.flashcardmobile.entity.CardTagCrossRef;
import com.example.flashcardmobile.entity.CardWithTags;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.entity.TagWithCards;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TagRepository {
    private final TagDao tagDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    public TagRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        tagDao = database.tagDao();
    }

    public void insert(Tag tag) {
        
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
    
    public LiveData<TagWithCards> getCardsForTag(long tagId) {
        return tagDao.getCardsForTag(tagId);
    }
    
    public LiveData<CardWithTags> getTagsForCard(long cardId) {
        return tagDao.getTagsForCard(cardId);
    }

    public CompletableFuture<Void> insertCrossRefs(List<CardTagCrossRef> crossRefs) {
        return CompletableFuture.runAsync(() -> tagDao.insertCrossRefs(crossRefs), executorService);
    }

    public void updateCrossRefs(long cardId, List<CardTagCrossRef> crossRefs) {
        CompletableFuture.runAsync(() -> tagDao.updateCrossRefs(cardId, crossRefs), executorService);
    }
}
