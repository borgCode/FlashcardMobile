package com.example.flashcardmobile.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.LearningAnalyticsDao;
import com.example.flashcardmobile.entity.LearningAnalytics;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LearningAnalyticsRepository {

    private LearningAnalyticsDao learningAnalyticsDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LearningAnalyticsRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        learningAnalyticsDao = database.learningAnalyticsDao();
    }

    public void insert(LearningAnalytics learningAnalytics) {
        CompletableFuture.runAsync(() -> learningAnalyticsDao.insert(learningAnalytics), executorService);
    }

    public void update(LearningAnalytics learningAnalytics) {
        CompletableFuture.runAsync(() -> learningAnalyticsDao.update(learningAnalytics), executorService);
    }

    public void delete(LearningAnalytics learningAnalytics) {
        CompletableFuture.runAsync(() -> learningAnalyticsDao.delete(learningAnalytics), executorService);
    }

    public CompletableFuture<LearningAnalytics> getAnalyticsByDate(LocalDate date) {
        return CompletableFuture.supplyAsync(() -> learningAnalyticsDao.getAnalyticsByDate(date), executorService);
    }

    public LiveData<List<LearningAnalytics>> getAnalyticsForMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return learningAnalyticsDao.getAnalyticsForMonth(startOfMonth, endOfMonth);
    }
}
