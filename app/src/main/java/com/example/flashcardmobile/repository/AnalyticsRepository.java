package com.example.flashcardmobile.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.AnalyticsDao;
import com.example.flashcardmobile.entity.DeckPerformance;
import com.example.flashcardmobile.entity.LearningAnalytics;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticsRepository {

    private AnalyticsDao analyticsDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AnalyticsRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        analyticsDao = database.learningAnalyticsDao();
    }

    public void insertLearningAnalytics(LearningAnalytics learningAnalytics) {
        CompletableFuture.runAsync(() -> analyticsDao.insertLearningAnalytics(learningAnalytics), executorService);
    }

    public void updateLearningAnalytics(LearningAnalytics learningAnalytics) {
        CompletableFuture.runAsync(() -> analyticsDao.updateLearningAnalytics(learningAnalytics), executorService);
    }

    public void deleteLearningAnalytics(LearningAnalytics learningAnalytics) {
        CompletableFuture.runAsync(() -> analyticsDao.deleteLearningAnalytics(learningAnalytics), executorService);
    }

    public CompletableFuture<LearningAnalytics> getAnalyticsByDate(LocalDate date) {
        return CompletableFuture.supplyAsync(() -> analyticsDao.getAnalyticsByDate(date), executorService);
    }

    public LiveData<List<LearningAnalytics>> getAnalyticsForMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return analyticsDao.getAnalyticsForMonth(startOfMonth, endOfMonth);
    }

    public void insertDeckPerformance(DeckPerformance deckPerformance) {
        CompletableFuture.runAsync(() -> analyticsDao.insertDeckPerformance(deckPerformance), executorService);
    }

    public void updateDeckPerformance(DeckPerformance deckPerformance) {
        CompletableFuture.runAsync(() -> analyticsDao.updateDeckPerformance(deckPerformance), executorService);
    }

    public void deleteDeckPerformance(DeckPerformance deckPerformance) {
        CompletableFuture.runAsync(() -> analyticsDao.deleteDeckPerformance(deckPerformance), executorService);
    }


    public CompletableFuture<DeckPerformance> getDeckPerformanceByDeckId(Long deckId) {
        return CompletableFuture.supplyAsync(() -> analyticsDao.getDeckPerformanceByDeckId(deckId), executorService);
    }
}
