package com.example.flashcardmobile.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.BadgeDao;
import com.example.flashcardmobile.entity.Badge;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BadgeRepository {
    private BadgeDao badgeDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    public BadgeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        badgeDao = database.badgeDao();
    }
    
    public void insert(Badge badge) {
        CompletableFuture.runAsync(() -> badgeDao.insert(badge), executorService);
    }

    public void update(Badge badge) {
        CompletableFuture.runAsync(() -> badgeDao.update(badge), executorService);
    }

    public CompletableFuture<Integer> hasBadges() {
        return CompletableFuture.supplyAsync(() -> badgeDao.countBadges(), executorService);
    }

    public LiveData<List<Badge>> getAllBadges() {
        return badgeDao.getAllBadges();
    }
}
