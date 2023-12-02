package com.example.flashcardmobile.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.CardDao;
import com.example.flashcardmobile.entity.Card;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepository {
    private CardDao cardDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CardRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cardDao = database.cardDao();

    }

    public CompletableFuture<Long> insert(Card card) {
        return CompletableFuture.supplyAsync(() -> cardDao.insert(card), executorService);
    }

    public void update(Card card) {
        CompletableFuture.runAsync(() -> cardDao.update(card), executorService);
    }

    public void delete(Card card) {
        CompletableFuture.runAsync(() -> cardDao.delete(card), executorService);
    }

    public void deleteById(long id) {
        CompletableFuture.runAsync(() -> cardDao.deleteById(id), executorService);

    }

    public LiveData<List<Card>> getAllDeckCards(long id) {
        return cardDao.getCardsByDeckId(id);
    }

    public LiveData<List<Card>> getAllDueCards(long id) {
        return cardDao.getDueCardsByDeckId(id, LocalDateTime.now().toString());
    }

    public LiveData<Card> getCardById(long id) {
        return cardDao.getCardById(id);
    }


}
