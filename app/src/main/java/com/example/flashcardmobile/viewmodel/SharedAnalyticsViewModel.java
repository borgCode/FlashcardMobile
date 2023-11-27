package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.LearningAnalytics;
import com.example.flashcardmobile.repository.LearningAnalyticsRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class SharedAnalyticsViewModel extends AndroidViewModel {
    
    private enum UpdateType {
        STUDIED,
        ADDED,
        MASTERED
    }
    
    private LocalDate date;
    private LearningAnalyticsRepository repository;

    public SharedAnalyticsViewModel(@NotNull Application application) {
        super(application);
        this.date = LocalDate.now();
        repository = new LearningAnalyticsRepository(application);
    }

    public void updateCardsStudied(int count) {
        updateAnalytics(UpdateType.STUDIED, count);
    }

    public void updateCardsAdded(int count) {
        updateAnalytics(UpdateType.ADDED, count);
    }

    public void updateCardsMastered(int count) {
        updateAnalytics(UpdateType.MASTERED, count);
    }
    
    private void updateAnalytics(UpdateType updateType, int count) {
        LearningAnalytics record = repository.getAnalyticsByDate(date);
        if (record != null) {
            switch (updateType) {
                case STUDIED:
                    record.setCardsStudied(record.getCardsStudied() + count);
                    break;
                case ADDED:
                    record.setCardsAdded(record.getCardsAdded() + count);
                    break;
                case MASTERED:
                    record.setCardsMastered(record.getCardsMastered() + count);
                    break;
            }
            repository.update(record);
        } else {
            int studied = (updateType == UpdateType.STUDIED) ? count : 0;
            int added = (updateType == UpdateType.ADDED) ? count : 0;
            int mastered = (updateType == UpdateType.MASTERED) ? count : 0;
            LearningAnalytics newRecord = new LearningAnalytics(date, studied, added, mastered);
            repository.insert(newRecord);
        }
    }

    public LiveData<List<LearningAnalytics>> getAnalyticsForMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return repository.getAnalyticsForMonth(startOfMonth, endOfMonth);
    }
    
}
