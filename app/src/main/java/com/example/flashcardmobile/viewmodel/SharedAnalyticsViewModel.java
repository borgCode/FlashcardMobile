package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.DeckPerformance;
import com.example.flashcardmobile.entity.LearningAnalytics;
import com.example.flashcardmobile.repository.AnalyticsRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class SharedAnalyticsViewModel extends AndroidViewModel {


    private LocalDate date;
    private AnalyticsRepository repository;

    public SharedAnalyticsViewModel(@NotNull Application application) {
        super(application);
        this.date = LocalDate.now();
        repository = new AnalyticsRepository(application);
    }

    public void updateCardsAdded(int count) {
        repository.getAnalyticsByDate(date).thenAccept(learningAnalytics -> {
            if (learningAnalytics != null) {
                learningAnalytics.setCardsAdded(learningAnalytics.getCardsAdded() + count);
            } else {
                LearningAnalytics newRecord = new LearningAnalytics(date, 0, count, 0, 0, 0, 0);
                repository.insertLearningAnalytics(newRecord);
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
                repository.updateLearningAnalytics(learningAnalytics);
            } else {
                LearningAnalytics newRecord = new LearningAnalytics(date, studied, 0, mastered, easy, medium, hard);
                repository.insertLearningAnalytics(newRecord);
            }
        });
    }

    public LiveData<List<LearningAnalytics>> getAnalyticsForMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return repository.getAnalyticsForMonth(startOfMonth, endOfMonth);
    }

    public void updateDeckPerformance(Long deckId, int numOfCardsStudied, int easyCounter, int mediumCounter, int hardCounter) {

        repository.getDeckPerformanceByDeckId(deckId).thenAccept(deckPerformance -> {
            if (deckPerformance != null) {
                deckPerformance.setCardsStudied(deckPerformance.getCardsStudied() + numOfCardsStudied);
                deckPerformance.setEasyAnswers(deckPerformance.getEasyAnswers() + easyCounter);
                deckPerformance.setMediumAnswers(deckPerformance.getMediumAnswers() + mediumCounter);
                deckPerformance.setHardAnswers(deckPerformance.getHardAnswers() + hardCounter);
                repository.updateDeckPerformance(deckPerformance);
            } else {
                DeckPerformance newDeckPerformance = new DeckPerformance(deckId, numOfCardsStudied, easyCounter, mediumCounter, hardCounter);
                repository.insertDeckPerformance(newDeckPerformance);
            }
        });
    }
}

