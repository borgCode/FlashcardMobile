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
    
    
    private LocalDate date;
    private LearningAnalyticsRepository repository;

    public SharedAnalyticsViewModel(@NotNull Application application) {
        super(application);
        this.date = LocalDate.now();
        repository = new LearningAnalyticsRepository(application);
    }
    
    public void updateCardsAdded(int count) {
        repository.getAnalyticsByDate(date).thenAccept(learningAnalytics -> {
            if (learningAnalytics != null) {
                learningAnalytics.setCardsAdded(learningAnalytics.getCardsAdded() + count);
            } else {
                LearningAnalytics newRecord = new LearningAnalytics(date, 0, count, 0, 0, 0, 0);
                repository.insert(newRecord);
            }
        });
        
    }
    
    public void updateAllAnalytics(int studied, int mastered, int easy, int medium, int hard) {
        repository.getAnalyticsByDate(date).thenAccept(learningAnalytics -> {
            if (learningAnalytics != null) {
                learningAnalytics.setCardsStudied(learningAnalytics.getCardsStudied() + studied);
                learningAnalytics.setCardsMastered(learningAnalytics.getCardsMastered() + mastered);
                learningAnalytics.setEasyCounter(learningAnalytics.getEasyCounter() + easy);
                learningAnalytics.setMediumCounter(learningAnalytics.getMediumCounter() + medium);
                learningAnalytics.setHardCounter(learningAnalytics.getHardCounter() + hard);
                repository.update(learningAnalytics);
            } else {
                LearningAnalytics newRecord = new LearningAnalytics(date, studied, 0, mastered, easy, medium, hard);
                repository.insert(newRecord);
            }
        });
    }
    
    public LiveData<List<LearningAnalytics>> getAnalyticsForMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return repository.getAnalyticsForMonth(startOfMonth, endOfMonth);
    }
    
}
