package com.example.flashcardmobile.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.DeckDao;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.entity.DeckWithInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeckRepository {

    private final DeckDao deckDao;
    private final LiveData<List<Deck>> allDecks;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DeckRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        deckDao = database.deckDao();
        allDecks = deckDao.getAllDecks();
    }

    public void insert(Deck deck) {
        
        CompletableFuture.runAsync(() -> deckDao.insert(deck), executorService);
    }

    public void update(Deck deck) {
        CompletableFuture.runAsync(() -> deckDao.update(deck), executorService);
    }

    public void delete(Deck deck) {
        CompletableFuture.runAsync(() -> deckDao.delete(deck), executorService);
    }
    
    public void deleteDeckById(long deckId) {
        CompletableFuture.runAsync(() -> deckDao.deleteDeckById(deckId), executorService);
    }
    public void deleteAllDecks() {
        CompletableFuture.runAsync(() -> deckDao.deleteAllDecks(), executorService);
    }

    public LiveData<List<Deck>> getAllDecks() {
        return allDecks;
    }

    public LiveData<Deck> getDeckById(long id) {
        return deckDao.getDeckById(id);
    }
    
    public LiveData<List<DeckWithInfo>> getAllDecksWithInfo(String now) {
        return deckDao.getAllDecksWithInfo(now);
    }

    public void updateDeckName(long deckId, String newName) {
        executorService.execute(() -> {
            Deck deck = deckDao.getDeckByIdSync(deckId);
            if (deck != null) {
                deck.setDeckName(newName);
                deckDao.update(deck);
            }
        });
    }
}
